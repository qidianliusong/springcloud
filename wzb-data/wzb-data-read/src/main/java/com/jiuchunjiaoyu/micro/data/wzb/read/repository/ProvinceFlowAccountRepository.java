package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ProvinceFlowAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceFlowAccountRepository extends JpaRepository<ProvinceFlowAccount, Long> {

    ProvinceFlowAccount findByProvinceName(String provinceName);
}
