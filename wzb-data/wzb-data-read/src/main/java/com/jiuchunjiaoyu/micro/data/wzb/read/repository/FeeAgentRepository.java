package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeeAgentRepository extends JpaRepository<FeeAgent, Long> {

    @Query(countQuery = "select count(f.feeAgentId) from FeeAgent f LEFT JOIN f.feeDetail d where f.status=:status and d.classId=:classId", value = "select f from FeeAgent f LEFT JOIN f.feeDetail d where f.status=:status and d.classId=:classId order by f.payTime desc")
    Page<FeeAgent> getPageInfo(@Param("classId") Long classId,@Param("status")Integer status, Pageable pageable);

    List<FeeAgent> findByFeeDetailId(Long feeDetailId);

    @Query("from FeeAgent where status=:status and payTime>:startTime")
    List<FeeAgent> findByStatus(@Param("status") int statusPrePay,@Param("startTime") Date startTime);
}
