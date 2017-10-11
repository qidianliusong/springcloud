package com.jiuchunjiaoyu.micro.wzb.task.task;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.constant.Constant;
import com.jiuchunjiaoyu.micro.wzb.task.dto.*;
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
public class FeePayTask {

    private static Logger logger = LoggerFactory.getLogger(FeePayTask.class);

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
            BaseResult<List<FeePayDTO>> feePayListResult = readFeign.getPreFeePays(DateUtil.getDateBefor(1));
            FeignUtil.validateFeignResult(feePayListResult, "readFeign.getPreFeePays", logger);

            List<FeePayDTO> feePayDTOList = feePayListResult.getData();

            if (feePayDTOList == null || feePayDTOList.isEmpty())
                return;

            feePayDTOList.parallelStream().forEach(feePayDTO -> {
                handleFeeAgentPay(feePayDTO);
            });


        } catch (Exception e) {
            logger.error("执行交费定时任务出错", e);
        }

    }

    private void handleFeeAgentPay(FeePayDTO feePayDTO) {

        if (feePayDTO == null || feePayDTO.getStatus() != FeePayDTO.STATUS_PRE_PAY)
            return;

        try {
            if (StringUtils.isEmpty(feePayDTO.getOutTradeNo())) {
                feePayDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                writeFeign.updateFeePay(feePayDTO);
                return;
            }

            QueryResultDTO queryResultDTO = gzhPayMng.query(feePayDTO.getOutTradeNo());

            WxPayQueryResponseDTO wxPayQueryResponseDTO = queryResultDTO.getWxPayQueryResponseDTO();

            if (!"SUCCESS".equals(wxPayQueryResponseDTO.getReturn_code())
                    || !"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())){
                if(Constant.WX_TRADE_QUERY_ORDERNOTEXIST.equals(wxPayQueryResponseDTO.getErr_code())){
                    feePayDTO.setStatus(FeePayDTO.STATUS_ERR);
                    BaseResult<FeePayDTO> update = writeFeign.updateFeePay(feePayDTO);
                    FeignUtil.validateFeignResult(update,"writeFeign.updateFeePay",logger);
                }
                return;
            }

            switch (wxPayQueryResponseDTO.getTrade_state()){
                case Constant.WX_TRADE_SUCCESS:
                    tradeSuccess(feePayDTO, wxPayQueryResponseDTO);
                    break;
                case Constant.WX_TRADE_USERPAYING:
                    break;
                case Constant.WX_TRADE_NOTPAY:
                    if(System.currentTimeMillis() < queryResultDTO.getWzbOrderDTO().getExpireTime().getTime())
                        break;
                case Constant.WX_TRADE_CLOSED:
                case Constant.WX_TRADE_PAYERROR:
                case Constant.WX_TRADE_REFUND:
                case Constant.WX_TRADE_REVOKED:
                    //此处支付异常
                    feePayDTO.setStatus(FeePayDTO.STATUS_ERR);
                    BaseResult<FeePayDTO> update = writeFeign.updateFeePay(feePayDTO);
                    FeignUtil.validateFeignResult(update,"writeFeign.updateFeePay",logger);
                    break;
                default:
                    break;
            }

        } catch (TaskException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("处理等待支付的代缴记录出错", e);
        }


    }

    /**
     * 支付成功
     * @throws TaskException
     */
    private void tradeSuccess(FeePayDTO feePayDTO, WxPayQueryResponseDTO wxPayQueryResponseDTO) throws TaskException {
        // 此处支付成功
        Integer totalFee = wxPayQueryResponseDTO.getTotal_fee();
        if (feePayDTO.getAmount().multiply(new BigDecimal(100)).intValue() != totalFee) {
            // 标记为支付异常
            feePayDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeePay(feePayDTO);
        } else {
            BaseResult<Void> afterFeePayResult = writeFeign.afterFeePay(null,feePayDTO.getChildId() ,feePayDTO.getFeePayId());
            FeignUtil.validateFeignResult(afterFeePayResult, "writeFeign.afterFeePay", logger);

            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(feePayDTO.getAmount());
            payMessageDTO.setId(feePayDTO.getFeePayId());
            payMessageDTO.setTradeNo(feePayDTO.getOutTradeNo());
            payMessageDTO.setType(PayMessageDTO.TYPE_FEE_PAY);
            rabbitTemplate.convertAndSend("wzb.topic","wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
    }

}
