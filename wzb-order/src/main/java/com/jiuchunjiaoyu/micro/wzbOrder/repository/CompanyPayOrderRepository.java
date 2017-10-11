package com.jiuchunjiaoyu.micro.wzbOrder.repository;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.CompanyPayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPayOrderRepository extends JpaRepository<CompanyPayOrder, Long> {

    CompanyPayOrder findByPartnerTradeNo(String partnerTradeNo);
}
