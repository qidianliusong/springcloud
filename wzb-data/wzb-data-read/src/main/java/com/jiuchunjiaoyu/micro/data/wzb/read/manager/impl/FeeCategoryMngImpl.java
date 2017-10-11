package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeCategory;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeCategoryMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeeCategoryRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeeCategoryMngImpl extends BaseMngImpl<FeeCategory> implements FeeCategoryMng {

    @Resource
    private FeeCategoryRepository feeCategoryRepository;

    @Override
    public List<FeeCategory> getCategoryList(Long schoolId, Long classId, Integer status) {
        Specification<FeeCategory> specification = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (status != null) {
                list.add(cb.equal(root.get("status"), status));
            }
            if (schoolId != null) {
                list.add(cb.equal(root.get("schoolId"), schoolId));
            } else {
                list.add(cb.isNull(root.get("schoolId")));
            }
            if (classId != null) {
                list.add(cb.equal(root.get("classId"), classId));
            } else {
                list.add(cb.isNull(root.get("classId")));
            }
            Predicate[] predicates = list.toArray(new Predicate[list.size()]);
            return cb.and(predicates);
        };
        return feeCategoryRepository.findAll(specification, new Sort(Sort.Direction.ASC, "priority"));
    }


    @Cacheable(value = "wzb:data:fee_category_basic", key = "'wzb:data:fee_category_basic_key'")
    public List<FeeCategory> getBasicCategory() {
        return feeCategoryRepository.getBasicCategory();
    }

    @Cacheable(value = "wzb:data:fee_category_by_school", key = "'wzb:data:fee_category_by_school_key'+#p0")
    public List<FeeCategory> getOpenCategoryBySchoolId(Long schoolId) {
        return feeCategoryRepository.getOpenCategoryBySchoolId(schoolId);
    }

    @Override
    public Page<FeeCategory> getCategoryPageBySchoolId(Long schoolId, Date startDate, Date endDate, Integer pageNo, Integer pageSize, boolean withGrade) {

        Pageable page = new PageRequest(pageNo, pageSize);
        Page<FeeCategory> feeCategoryPage = feeCategoryRepository.getCategoryBySchoolId(schoolId, startDate, endDate, page);

        if (!withGrade)
            return feeCategoryPage;

        handleGrade(feeCategoryPage);

        return feeCategoryPage;
    }

    private void handleGrade(Page<FeeCategory> feeCategoryPage) {
        if (feeCategoryPage.getContent() == null || feeCategoryPage.getContent().isEmpty())
            return;

        feeCategoryPage.getContent().stream().forEach(feeCategory -> {
            if (feeCategory.getFeeCategoryGradeSet() != null && !feeCategory.getFeeCategoryGradeSet().isEmpty()) {
                if (feeCategory.getFeeCategoryGrades() == null)
                    feeCategory.setFeeCategoryGrades(new ArrayList<>());
                feeCategory.getFeeCategoryGrades().addAll(feeCategory.getFeeCategoryGradeSet());
            }
        });
    }

    @Override
    public Page<FeeCategory> getCategoryPageBySchoolId(Long schoolId, Integer pageNo, Integer pageSize, boolean withGrade) {
        Pageable page = new PageRequest(pageNo, pageSize, new Sort(Sort.Direction.DESC, "createDate"));
        Page<FeeCategory> feeCategoryPage = feeCategoryRepository.findBySchoolId(schoolId, page);
        if (!withGrade)
            return feeCategoryPage;
        handleGrade(feeCategoryPage);
        return feeCategoryPage;
    }

    @Override
    public List<FeeCategory> getCategoryBySchoolIdAndGradeId(Long schoolId, Long gradeId) {
        return feeCategoryRepository.getCategoryBySchoolIdAndGradeId(schoolId, gradeId);
    }

    @Cacheable(value = "wzb:data:fee_category_by_class", key = "'wzb:data:fee_category_by_class_key'+#p0")
    public List<FeeCategory> getCategoryByClassId(Long classId) {
        return feeCategoryRepository.getCategoryByClassId(classId);
    }

    @Override
    protected JpaRepository<FeeCategory, Long> getRepository() {
        return feeCategoryRepository;
    }

}
