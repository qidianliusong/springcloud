package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ProvinceFlowAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceFlowAccountRepository extends CrudRepository<ProvinceFlowAccount, Long> {

    ProvinceFlowAccount findByProvinceName(String provinceName);
}
