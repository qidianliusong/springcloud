package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeeCategoryRepository extends JpaRepository<FeeCategory, Long>,JpaSpecificationExecutor<FeeCategory> {

    @Query("from FeeCategory where classId=:classId order by priority")
    List<FeeCategory> getCategoryByClassId(@Param("classId") Long classId);

    @Query("from FeeCategory where status=0 and schoolId=:schoolId order by priority")
    List<FeeCategory> getOpenCategoryBySchoolId(@Param("schoolId") Long schoolId);

    @Query(value="from FeeCategory where createDate between :startDate and :endDate and schoolId=:schoolId order by createDate desc,priority asc",
            countQuery = "select count(*) from FeeCategory where createDate between :startDate and :endDate and schoolId=:schoolId")
    Page<FeeCategory> getCategoryBySchoolId(@Param("schoolId") Long schoolId,@Param("startDate") Date startDate,@Param("endDate") Date endDate, Pageable page);

    @Query("from FeeCategory where status=0 and schoolId is null and classId is null order by priority")
    List<FeeCategory> getBasicCategory();

    @Query(nativeQuery = true, value = "SELECT f.* from fee_category f left join fee_category_grade g on f.fee_category_id=g.fee_category_id where (f.all_grade = true or g.grade_id=:gradeId) and f.status=0 and f.school_id=:schoolId order by priority")
    List<FeeCategory> getCategoryBySchoolIdAndGradeId(@Param("schoolId") Long schoolId, @Param("gradeId") Long gradeId);

    Page<FeeCategory> findBySchoolId(Long schoolId, Pageable page);
}
