package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public interface SchoolDrawRecordRepository extends JpaRepository<SchoolDrawRecord, Long>, JpaSpecificationExecutor<SchoolDrawRecord> {

    Page<SchoolDrawRecord> findBySchoolId(long schoolId, Pageable page);

    @Query(value = "from SchoolDrawRecord where drawTime between :startDate and :endDate and schoolId=:schoolId",
            countQuery = "select count(*) from SchoolDrawRecord where drawTime between :startDate and :endDate and schoolId=:schoolId")
    Page<SchoolDrawRecord> findBySchoolId(@Param("schoolId") long schoolId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);

    @Query(value = "select sum(amount) from SchoolDrawRecord where status=2 and createTime between :startDate and :endDate")
    BigDecimal getSum(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}