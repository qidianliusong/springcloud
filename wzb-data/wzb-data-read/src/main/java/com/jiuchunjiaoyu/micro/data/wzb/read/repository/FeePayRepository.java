package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;

@Repository
public interface FeePayRepository extends JpaRepository<FeePay, Long> {

	@Query("from FeePay where feeDetailId=:feeDetailId")
	List<FeePay> findByFeeDetailId(@Param("feeDetailId") Long feeDetailId);

	@Query("from FeePay where status in (:payStatus) and feeDetailId=:feeDetailId ")
	List<FeePay> findByFeeDetailId(@Param("feeDetailId") Long feeDetailId, @Param("payStatus") Integer... payStatus);

	FeePay findByFeeDetailIdAndChildId(Long feeDetailId, Long childId);

	@Query("from FeePay where status=:status and payTime>:startTime")
    List<FeePay> findByStatus(@Param("status") int statusPrePay, @Param("startTime") Date startTime);
}
