package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;

@Repository
public interface FeeTakeRepository extends CrudRepository<FeeTake, Long>{

	@Query("select sum(amount) from FeeTake where classId=:classId")
	BigDecimal getSumAmount(@Param("classId") Long classId);
	
}
