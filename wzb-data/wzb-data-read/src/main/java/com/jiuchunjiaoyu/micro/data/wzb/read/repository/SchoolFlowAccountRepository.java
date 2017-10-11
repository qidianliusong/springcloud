package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolFlowAccountRepository extends JpaRepository<SchoolFlowAccount, Long> {

    SchoolFlowAccount findBySchoolId(Long schoolId);
}
