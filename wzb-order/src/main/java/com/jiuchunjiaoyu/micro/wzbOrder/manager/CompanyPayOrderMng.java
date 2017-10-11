package com.jiuchunjiaoyu.micro.wzbOrder.manager;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.CompanyPayOrder;

public interface CompanyPayOrderMng {

    CompanyPayOrder saveOrUpdate(CompanyPayOrder commpanyPayOrder);

    CompanyPayOrder findById(Long commpanyPayOrderId);

    CompanyPayOrder findByPartnerTradeNo(String partnerTradeNo);

}
