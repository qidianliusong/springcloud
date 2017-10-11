package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolFlowAccountRepository extends CrudRepository<SchoolFlowAccount, Long> {

    SchoolFlowAccount findBySchoolId(Long schoolId);
}
