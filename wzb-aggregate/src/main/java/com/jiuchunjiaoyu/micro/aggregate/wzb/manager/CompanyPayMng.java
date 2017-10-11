package com.jiuchunjiaoyu.micro.aggregate.wzb.manager;

import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.CompanyPayOrderDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.CompanyPayResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.PrePayDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.WxPayQueryResponseDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.dto.wx.PayCallBackRequestDTO;
import com.jiuchunjiaoyu.micro.aggregate.wzb.exception.WzbAggregateException;

import java.math.BigDecimal;

/**
 * 企业支付相关
 */
public interface CompanyPayMng {

    CompanyPayResponseDTO draw(CompanyPayOrderDTO companyPayOrderDTO) throws WzbAggregateException;

}
