package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;

import java.util.Date;
import java.util.List;

@Repository
public interface FeeDrawRecordRepository extends JpaRepository<FeeDrawRecord, Long> {

    @Query(countQuery = "select count(*) from FeeDrawRecord where classId=:classId", value = "from FeeDrawRecord where classId=:classId order by createTime desc")
    Page<FeeDrawRecord> getPage(@Param("classId") Long classId, Pageable page);

    @Query(countQuery = "select count(*) from FeeDrawRecord where status=:status and classId=:classId", value = "from FeeDrawRecord where status=:status and classId=:classId order by createTime desc")
    Page<FeeDrawRecord> getPage(@Param("classId") Long classId, @Param("status") Integer status, Pageable page);

    @Query(countQuery = "select count(*) from FeeDrawRecord where status in (:status) and classId=:classId", value = "from FeeDrawRecord where status in (:status) and classId=:classId order by createTime desc")
    Page<FeeDrawRecord> getPage(@Param("classId") Long classId, @Param("status") Integer[] status, Pageable page);

    @Query("from FeeDrawRecord where status=:status and createTime>:startDate")
    List<FeeDrawRecord> getListByStatus(@Param("status") Integer status, @Param("startDate") Date startDate);

    @Query("select count(*) from FeeDrawRecord where status=:status and classId=:classId and createTime>:startDate")
    int countByStatus(@Param("status") Integer status, @Param("startDate") Date startDate,@Param("classId") Long classId);
}
