package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;

@Repository
public interface FeeTakeRepository extends JpaRepository<FeeTake, Long>{

	@Query(value="from FeeTake where classId=:classId",countQuery="select count(*) from FeeTake where classId=:classId")
	Page<FeeTake> findByClassId(@Param("classId") Long classId,Pageable page);
	
}
