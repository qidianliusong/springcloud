package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountFlowRepository extends CrudRepository<AccountFlow, Long> {

    @Query("select count(*)>0 from AccountFlow where tradeNo=:tradeNo")
    boolean existByTradeNo(@Param("tradeNo") String tradeNo);

}
