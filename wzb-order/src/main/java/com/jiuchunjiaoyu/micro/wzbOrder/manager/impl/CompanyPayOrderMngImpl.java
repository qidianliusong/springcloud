package com.jiuchunjiaoyu.micro.wzbOrder.manager.impl;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.CompanyPayOrder;
import com.jiuchunjiaoyu.micro.wzbOrder.manager.CompanyPayOrderMng;
import com.jiuchunjiaoyu.micro.wzbOrder.repository.CompanyPayOrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CompanyPayOrderMngImpl implements CompanyPayOrderMng {

    @Resource
    private CompanyPayOrderRepository commpanyPayOrderRepository;

    @Override
    public CompanyPayOrder saveOrUpdate(CompanyPayOrder commpanyPayOrder) {
        return commpanyPayOrderRepository.save(commpanyPayOrder);
    }

    @Override
    public CompanyPayOrder findById(Long commpanyPayOrderId) {
        return commpanyPayOrderRepository.findOne(commpanyPayOrderId);
    }

    @Override
    public CompanyPayOrder findByPartnerTradeNo(String partnerTradeNo) {
        return commpanyPayOrderRepository.findByPartnerTradeNo(partnerTradeNo);
    }
}
