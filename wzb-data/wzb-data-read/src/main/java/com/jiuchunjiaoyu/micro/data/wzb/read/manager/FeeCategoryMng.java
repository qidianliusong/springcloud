package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import java.util.Date;
import java.util.List;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import org.springframework.data.domain.Page;

public interface FeeCategoryMng extends BaseMng<FeeCategory>{

	List<FeeCategory> getCategoryList(Long schoolId, Long classId,Integer status);

    List<FeeCategory> getBasicCategory();

    List<FeeCategory> getCategoryByClassId(Long classId);

    List<FeeCategory> getOpenCategoryBySchoolId(Long schoolId);

    Page<FeeCategory> getCategoryPageBySchoolId(Long schoolId, Date startDate,Date endDate,Integer pageNo,Integer pageSize,boolean withGrade);

    Page<FeeCategory> getCategoryPageBySchoolId(Long schoolId, Integer pageNo, Integer pageSize, boolean withGrade);

    List<FeeCategory> getCategoryBySchoolIdAndGradeId(Long schoolId,Long gradeId);
}
