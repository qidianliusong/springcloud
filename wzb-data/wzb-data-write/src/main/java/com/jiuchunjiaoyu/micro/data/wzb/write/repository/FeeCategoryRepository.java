package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeCategoryRepository extends CrudRepository<FeeCategory, Long> {

    @Query("select max(priority) from FeeCategory")
    Integer getMaxPriority();

    @Query("select count(*)>0 from FeeCategory where feeCategoryName=:feeCategoryName and schoolId is null and classId is null")
    boolean existByFeeCategoryName(@Param("feeCategoryName") String feeCategoryName);
}
