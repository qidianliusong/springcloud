package com.jiuchunjiaoyu.micro.aggregate.wzb.manager;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.PrePayDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.QueryResultDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;

import java.math.BigDecimal;

/**
 * h5支付相关
 */
public interface GzhPayMng {

    PrePayDTO feePrePay(BigDecimal amount,String openid ,String ip) throws WzbAggregateException;

    PrePayDTO agentPrePay(BigDecimal amount,String openid ,String ip) throws WzbAggregateException;

    QueryResultDTO query(String getOutTradeNo) throws WzbAggregateException;

    void callBack(PayCallBackRequestDTO payCallBackRequestDTO) throws WzbAggregateException;

}
