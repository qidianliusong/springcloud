package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassAccountRepository extends JpaRepository<ClassAccount, Long> {

    ClassAccount findByClassId(Long classId);

    @Query("select count(*)>0 from ClassAccount where classId=:classId")
    boolean existsByClassId(@Param("classId") Long classId);

    Page<ClassAccount> findBySchoolId(Long schoolId, Pageable page);
}
