package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeeDetailRepository extends JpaRepository<FeeDetail, Long> {

    List<FeeDetail> findByClassId(Long classId);

    @Query(value = "from FeeDetail where classId=:classId", countQuery = "select count(*) from FeeDetail where classId=:classId")
    Page<FeeDetail> getPageInfoByClassId(@Param("classId") Long classId, Pageable page);

    @Query(value = "from FeeDetail where status=:status and classId=:classId", countQuery = "select count(*) from FeeDetail where status=:status and classId=:classId")
    Page<FeeDetail> getPageInfoByClassIdAndStatus(@Param("classId") Long classId, @Param("status") Integer status, Pageable page);

    @Query(value = "from FeeDetail where feeCategoryId=:categoryId", countQuery = "select count(*) from FeeDetail where feeCategoryId=:categoryId")
    Page<FeeDetail> getPageInfoByCategoryId(@Param("categoryId") Long categoryId, Pageable page);

    @Query(value = "select sum(totalAmount) from FeeDetail where feeCategoryId=:categoryId ")
    BigDecimal getSumByCategoryId(@Param("categoryId") Long categoryId);
}
