package com.jiuchunjiaoyu.micro.aggregate.wzb.manager.impl;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WxTradeStaus;
import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.PrePayDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.QueryResultDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WzbOrderDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.GzhPayFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.WzbOrderFeign;
import com.jiuchunjiaoyu.micro.aggregate.wzb.manager.GzhPayMng;
import com.jiuchunjiaoyu.micro.aggregate.wzb.util.FeignUtil;
import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.base.common.component.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;

@Service
public class GzhPayMngImpl implements GzhPayMng {

    private static Logger logger = LoggerFactory.getLogger(GzhPayMngImpl.class);
    /**
     * 订单过期时间10分钟
     */
    private static final Integer ORDER_EXPIRE_TIME = 600;

    private static final String DATE_FORMAT_FOR_RANDOM = "yyyyMMddHHmmssSSS";
    private static final int RANDOM_LEN = 10;

    @Value("${pay.body}")
    private String payBody;
    @Value("${pay.detail}")
    private String payDetail;

    @Value("${pay.agent.body}")
    private String agentBody;
    @Value("${pay.agent.detail}")
    private String agentDetail;

    @Value("${wx.callback.url}")
    private String notifyUrl;

    @Resource
    private GzhPayFeign gzhPayFeign;

    @Resource
    private WzbOrderFeign wzbOrderFeign;

    @Override
    public PrePayDTO feePrePay(BigDecimal amount, String openid, String ip) throws WzbAggregateException {
        return prePay(amount, WzbOrderDTO.TYPE_CLASS_FEE_PAY, openid, ip, payBody, payDetail);
    }

    private PrePayDTO prePay(BigDecimal amount, Integer orderType, String openid, String ip, String body, String detail) throws WzbAggregateException {
        WzbOrderDTO wzbOrder = new WzbOrderDTO();
        String outTradeNo = RandomUtil.createRandomStrByTime(DATE_FORMAT_FOR_RANDOM, RANDOM_LEN);
        wzbOrder.setAppid(WzbConstant.GZH_APP_ID);
        wzbOrder.setBody(body);
        wzbOrder.setDetail(detail);
        wzbOrder.setOutTradeNo(outTradeNo);
        wzbOrder.setTotalFee(amount.multiply(new BigDecimal(100)).intValue());
        wzbOrder.setOpenid(openid);
        wzbOrder.setSpbillCreateIp(ip);
        wzbOrder.setWzbOrderType(orderType);
        Calendar c = Calendar.getInstance();
        wzbOrder.setCreateTime(c.getTime());
        c.add(Calendar.SECOND, ORDER_EXPIRE_TIME);
        wzbOrder.setExpireTime(c.getTime());

        BaseResult<WzbOrderDTO> wzbOrderDTOResult = wzbOrderFeign.saveOrUpdate(wzbOrder);

        FeignUtil.validateFeignResult(wzbOrderDTOResult, "wzbOrderFeign.saveOrUpdate", logger);

        WzbOrderDTO wzbOrderDTO = wzbOrderDTOResult.getData();
        wzbOrderDTO.setNotifyUrl(notifyUrl);

        BaseResult<PrePayDTO> prePayDTOResult = gzhPayFeign.prePay(wzbOrderDTO);
        FeignUtil.validateFeignResult(prePayDTOResult, "gzhPayFeign.prePay", logger);

        PrePayDTO prePayDTO = prePayDTOResult.getData();

        prePayDTO.setWzbOrderDTO(wzbOrderDTO);
        return prePayDTO;
    }

    @Override
    public PrePayDTO agentPrePay(BigDecimal amount, String openid, String ip) throws WzbAggregateException {
        return prePay(amount, WzbOrderDTO.TYPE_CLASS_FEE_AGENT, openid, ip, agentBody, agentDetail);
    }

    @Override
    public QueryResultDTO query(String outTradeNo) throws WzbAggregateException {
        BaseResult<WxPayQueryResponseDTO> wxPayQueryResponseDTOBaseResult = gzhPayFeign.query(outTradeNo);
        FeignUtil.validateFeignResult(wxPayQueryResponseDTOBaseResult, "gzhPayFeign.query", logger);
        BaseResult<WzbOrderDTO> wzbOrderDTOBaseResult = wzbOrderFeign.findByOutTradeNo(outTradeNo);
        FeignUtil.validateFeignResult(wzbOrderDTOBaseResult, "wzbOrderFeign.findByOutTradeNo", logger);
        WxPayQueryResponseDTO wxPayQueryResponseDTO = wxPayQueryResponseDTOBaseResult.getData();
        WzbOrderDTO wzbOrderDTO = wzbOrderDTOBaseResult.getData();
//        if (WzbOrderDTO.STATUS_PAY == wzbOrderDTO.getStatus()) {
//            return wxPayQueryResponseDTO;
//        }

        QueryResultDTO queryResultDTO = new QueryResultDTO();
        queryResultDTO.setWxPayQueryResponseDTO(wxPayQueryResponseDTO);
        queryResultDTO.setWzbOrderDTO(wzbOrderDTO);

        if (!"SUCCESS".equals(wxPayQueryResponseDTO.getResult_code())) {
            if("ORDERNOTEXIST".equals(wxPayQueryResponseDTO.getErr_code())){
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(wxPayQueryResponseDTO.getErr_code()+":"+wxPayQueryResponseDTO.getErr_code_des());
                BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
                FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);
            }
            return queryResultDTO;
        }

        wzbOrderDTO.setTransactionId(wxPayQueryResponseDTO.getTransaction_id());

        switch (wxPayQueryResponseDTO.getTrade_state()){
            case WzbConstant.WX_TRADE_SUCCESS:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_PAY);
                wzbOrderDTO.setMessage(WxTradeStaus.success.getCode()+"_"+WxTradeStaus.success.getMesssage());
                break;
            case WzbConstant.WX_TRADE_USERPAYING:
                return queryResultDTO;
            case WzbConstant.WX_TRADE_NOTPAY:
                if(System.currentTimeMillis() <  wzbOrderDTO.getExpireTime().getTime())
                    return queryResultDTO;
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.notpay.getCode()+"_"+WxTradeStaus.notpay.getMesssage());
                break;
            case WzbConstant.WX_TRADE_CLOSED:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.close.getCode()+"_"+WxTradeStaus.close.getMesssage());
                break;
            case WzbConstant.WX_TRADE_PAYERROR:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.payerror.getCode()+"_"+WxTradeStaus.payerror.getMesssage());
                break;
            case WzbConstant.WX_TRADE_REFUND:
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.refund.getCode()+"_"+WxTradeStaus.refund.getMesssage());
                break;
            case WzbConstant.WX_TRADE_REVOKED:
                //此处支付异常
                wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
                wzbOrderDTO.setMessage(WxTradeStaus.revoked.getCode()+"_"+WxTradeStaus.revoked.getMesssage());
                break;
            default:
                break;
        }
        BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
        FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);

        return queryResultDTO;

    }

    @Override
    public void callBack(PayCallBackRequestDTO payCallBackRequestDTO) throws WzbAggregateException {
        BaseResult<WzbOrderDTO> wzbOrderDTOBaseResult = wzbOrderFeign.findByOutTradeNo(payCallBackRequestDTO.getOut_trade_no());
        FeignUtil.validateFeignResult(wzbOrderDTOBaseResult, "wzbOrderFeign.findByOutTradeNo", logger);
        WzbOrderDTO wzbOrderDTO = wzbOrderDTOBaseResult.getData();

        if (WzbOrderDTO.STATUS_PAY == wzbOrderDTO.getStatus()) {
            return;
        }

        if (!"SUCCESS".equals(payCallBackRequestDTO.getResult_code())) {
            wzbOrderDTO.setMessage(payCallBackRequestDTO.getErr_code() + ":" + payCallBackRequestDTO.getErr_code_des());
            wzbOrderDTO.setTransactionId(payCallBackRequestDTO.getTransaction_id());
            wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_FAIL);
            BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
            FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);
            return;
        }

        wzbOrderDTO.setMessage("SUCCESS");
        wzbOrderDTO.setTransactionId(payCallBackRequestDTO.getTransaction_id());
        wzbOrderDTO.setStatus(WzbOrderDTO.STATUS_PAY);
        BaseResult<WzbOrderDTO> wzbOrderDTOUpdateResult = wzbOrderFeign.saveOrUpdate(wzbOrderDTO);
        FeignUtil.validateFeignResult(wzbOrderDTOUpdateResult, "wzbOrderFeign.saveOrUpdate", logger);
    }
}
