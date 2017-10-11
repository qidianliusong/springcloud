package com.jiuchunjiaoyu.micro.wzb.task.task;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.constant.Constant;
import com.jiuchunjiaoyu.micro.wzb.task.dto.CompanyPayQueryRpDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.FeeDrawRecordDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.PayMessageDTO;
import com.jiuchunjiaoyu.micro.wzb.task.exception.TaskException;
import com.jiuchunjiaoyu.micro.wzb.task.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.task.feign.WzbDataWriteFeign;
import com.jiuchunjiaoyu.micro.wzb.task.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.wzb.task.util.DateUtil;
import com.jiuchunjiaoyu.micro.wzb.task.util.FeignUtil;
import com.jiuchunjiaoyu.micro.wzb.task.util.GsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CompanyPayTask {

    private static Logger logger = LoggerFactory.getLogger(CompanyPayTask.class);

    @Autowired
    private WzbDataReadFeign readFeign;

    @Autowired
    private WzbDataWriteFeign writeFeign;

    @Autowired
    private GzhPayMng gzhPayMng;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 30000)
    public void payTask() {

        try {
            BaseResult<List<FeeDrawRecordDTO>> prePayRecords = readFeign.getPrePayRecords(DateUtil.getDateBefor(1));
            FeignUtil.validateFeignResult(prePayRecords, "readFeign.getPrePayRecords", logger);

            List<FeeDrawRecordDTO> feeDrawRecordDTOS = prePayRecords.getData();

            if (feeDrawRecordDTOS == null || feeDrawRecordDTOS.isEmpty())
                return;

            feeDrawRecordDTOS.parallelStream().forEach(feeDrawRecordDTO -> {
                handleFeeAgentPay(feeDrawRecordDTO);
            });


        } catch (Exception e) {
            logger.error("执行交费定时任务出错", e);
        }

    }

    private void handleFeeAgentPay(FeeDrawRecordDTO feeDrawRecordDTO) {

        if (feeDrawRecordDTO == null || feeDrawRecordDTO.getStatus() != FeeDrawRecordDTO.STATUS_NOT_PAY)
            return;

        try {
            if (StringUtils.isEmpty(feeDrawRecordDTO.getPartnerTradeNo())) {
                feeDrawRecordDTO.setStatus(FeeDrawRecordDTO.STATUS_PAY_FAIL);
                writeFeign.saveOrUpdateFeeDrawRecord(feeDrawRecordDTO);
                return;
            }

            CompanyPayQueryRpDTO companyPayQueryRpDTO = gzhPayMng.queryCompanyPay(feeDrawRecordDTO.getPartnerTradeNo());

            if (!Constant.WX_TRADE_SUCCESS.equals(companyPayQueryRpDTO.getResultCode())) {
                if (Constant.WX_COMPANY_PAY_QUERY_NOT_FOUND.equals(companyPayQueryRpDTO.getErrCode())) {
                    feeDrawRecordDTO.setStatus(FeeDrawRecordDTO.STATUS_PAY_FAIL);
                    writeFeign.saveOrUpdateFeeDrawRecord(feeDrawRecordDTO);
                }
                return;
            }

            switch (companyPayQueryRpDTO.getStatus()) {
                case "SUCCESS":
                    tradeSuccess(feeDrawRecordDTO, companyPayQueryRpDTO);
                    return;
                case "FAILED":
                    feeDrawRecordDTO.setStatus(FeeDrawRecordDTO.STATUS_PAY_FAIL);
                    break;

                case "PROCESSING":
                    return;
                default:
                    return;
            }
            writeFeign.saveOrUpdateFeeDrawRecord(feeDrawRecordDTO);

        } catch (TaskException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("处理等待支付的代缴记录出错", e);
        }

    }

    /**
     * 支付成功
     *
     * @throws TaskException
     */
    private void tradeSuccess(FeeDrawRecordDTO feeDrawRecordDTO, CompanyPayQueryRpDTO companyPayQueryRpDTO) throws TaskException {
        // 此处支付成功
        Integer totalFee = companyPayQueryRpDTO.getPaymentAmount();
        if (feeDrawRecordDTO.getAmount().multiply(new BigDecimal(100)).intValue() != totalFee) {
            // 标记为支付异常
            logger.error("严重错误：取款记录金额与实际取款金额不一致,feeDrawRecordId=" + feeDrawRecordDTO.getFeeDrawRecordId());
            feeDrawRecordDTO.setStatus(FeeDrawRecordDTO.STATUS_PAY_FAIL);
            writeFeign.saveOrUpdateFeeDrawRecord(feeDrawRecordDTO);
        } else {
            BaseResult<Void> afterFeePayResult = writeFeign.afterCompanyPay(feeDrawRecordDTO.getFeeDrawRecordId());
            FeignUtil.validateFeignResult(afterFeePayResult, "writeFeign.afterFeePay", logger);
            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(feeDrawRecordDTO.getAmount());
            payMessageDTO.setId(feeDrawRecordDTO.getFeeDrawRecordId());
            payMessageDTO.setTradeNo(feeDrawRecordDTO.getPartnerTradeNo());
            payMessageDTO.setType(PayMessageDTO.TYPE_DRAW);
            rabbitTemplate.convertAndSend("wzb.topic", "wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
    }

}
