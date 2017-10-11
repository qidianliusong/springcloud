package com.jiuchunjiaoyu.micro.wzb.task.task;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.task.constant.Constant;
import com.jiuchunjiaoyu.micro.wzb.task.dto.FeeAgentDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.PayMessageDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.QueryResultDTO;
import com.jiuchunjiaoyu.micro.wzb.task.dto.WxPayQueryResponseDTO;
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
public class FeeAgentTask {

    private static Logger logger = LoggerFactory.getLogger(FeeAgentTask.class);

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
            BaseResult<List<FeeAgentDTO>> feeAgentListResult = readFeign.getPreFeeAgents(DateUtil.getDateBefor(1));
            FeignUtil.validateFeignResult(feeAgentListResult, "readFeign.getPreFeeAgents", logger);

            List<FeeAgentDTO> feeAgentDTOList = feeAgentListResult.getData();

            if (feeAgentDTOList == null || feeAgentDTOList.isEmpty())
                return;

            feeAgentDTOList.parallelStream().forEach(feeAgentDTO -> {
                handleFeeAgentPay(feeAgentDTO);
            });


        } catch (Exception e) {
            logger.error("执行代缴费定时任务出错", e);
        }

    }

    private void handleFeeAgentPay(FeeAgentDTO feeAgentDTO) {

        if (feeAgentDTO == null || feeAgentDTO.getStatus() != FeeAgentDTO.STATUS_PRE_PAY)
            return;

        try {
            if (StringUtils.isEmpty(feeAgentDTO.getOutTradeNo())) {
                feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                writeFeign.updateFeeAgent(feeAgentDTO);
            }

            QueryResultDTO queryResultDTO = gzhPayMng.query(feeAgentDTO.getOutTradeNo());
            WxPayQueryResponseDTO wxPayQueryResponseDTO = queryResultDTO.getWxPayQueryResponseDTO();

            if (!"SUCCESS".equals(wxPayQueryResponseDTO.getReturn_code())
                    || !"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())){
                if(Constant.WX_TRADE_QUERY_ORDERNOTEXIST.equals(wxPayQueryResponseDTO.getErr_code())){
                    feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                    writeFeign.updateFeeAgent(feeAgentDTO);
                }
                return;
            }
            switch (wxPayQueryResponseDTO.getTrade_state()){
                case Constant.WX_TRADE_SUCCESS:
                    tradeSuccess(feeAgentDTO, wxPayQueryResponseDTO);
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
                    feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
                    writeFeign.updateFeeAgent(feeAgentDTO);
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
     * @param feeAgentDTO
     * @param wxPayQueryResponseDTO
     * @throws TaskException
     */
    private void tradeSuccess(FeeAgentDTO feeAgentDTO, WxPayQueryResponseDTO wxPayQueryResponseDTO) throws TaskException {
        // 此处支付成功
        Integer totalFee = wxPayQueryResponseDTO.getTotal_fee();
        if (feeAgentDTO.getTotalAmount().multiply(new BigDecimal(100)).intValue() != totalFee) {
            // 标记为支付异常
            feeAgentDTO.setStatus(FeeAgentDTO.STATUS_ERR);
            writeFeign.updateFeeAgent(feeAgentDTO);
        } else {
            BaseResult<Void> afterFeeAgentResult = writeFeign.afterFeeAgent(null, feeAgentDTO.getFeeAgentId());
            FeignUtil.validateFeignResult(afterFeeAgentResult, "writeFeign.afterFeeAgent", logger);
            PayMessageDTO payMessageDTO = new PayMessageDTO();
            payMessageDTO.setAmount(feeAgentDTO.getTotalAmount());
            payMessageDTO.setId(feeAgentDTO.getFeeAgentId());
            payMessageDTO.setTradeNo(feeAgentDTO.getOutTradeNo());
            payMessageDTO.setType(PayMessageDTO.TYPE_FEE_AGENT);
            rabbitTemplate.convertAndSend("wzb.topic","wzb.pay.message", GsonUtil.GSON.toJson(payMessageDTO));
        }
    }

}
