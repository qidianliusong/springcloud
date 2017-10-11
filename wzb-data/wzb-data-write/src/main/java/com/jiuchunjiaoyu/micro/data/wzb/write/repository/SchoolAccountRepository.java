package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolAccountRepository extends CrudRepository<SchoolAccount, Long> {

    SchoolAccount findBySchoolId(Long schoolId);

    @Query("select count(*)>0 from SchoolAccount where schoolId=:schoolId")
    boolean existsBySchoolId(@Param("schoolId") Long schoolId);
}
