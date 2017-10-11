package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.SchoolDrawRecordMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.SchoolDrawRecordRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SchoolDrawRecordMngImpl extends BaseMngImpl<SchoolDrawRecord> implements SchoolDrawRecordMng {

    @Resource
    private SchoolDrawRecordRepository schoolDrawRecordRepository;

    @Override
    protected JpaRepository<SchoolDrawRecord, Long> getRepository() {
        return schoolDrawRecordRepository;
    }

    @Override
    public Page<SchoolDrawRecord> getPageBySchoolId(Long schoolId, Integer status, Integer pageNo, Integer pageSize) {

        Pageable page = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "createTime");

        SchoolDrawRecord schoolDrawRecord = new SchoolDrawRecord();
        if (schoolId != null)
            schoolDrawRecord.setSchoolId(schoolId);
        if (status != null)
            schoolDrawRecord.setStatus(status);

        Example<SchoolDrawRecord> example = Example.of(schoolDrawRecord);

        return schoolDrawRecordRepository.findAll(example, page);
    }

    @Override
    public Page<SchoolDrawRecord> getPageBySchoolId(Long schoolId, Integer status, Date startDate, Date endDate, Integer pageNo, Integer pageSize) {
        Pageable page = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "createTime");

        Specification<SchoolDrawRecord> specification = (root, query, cb) -> {
            List<Predicate> lstPredicates = new ArrayList<Predicate>();
            if (schoolId != null) {
                lstPredicates.add(cb.equal(root.get("schoolId"), schoolId));
            }
            if (status != null)
                lstPredicates.add(cb.equal(root.get("status"), status));
            if (startDate != null && endDate != null) {
                lstPredicates.add(cb.between(root.get("createTime"), startDate, endDate));
            }
            Predicate[] arrayPredicates = new Predicate[lstPredicates.size()];
            return cb.and(lstPredicates.toArray(arrayPredicates));
        };

        return schoolDrawRecordRepository.findAll(specification, page);
    }

    @Override
    public Page<SchoolDrawRecord> getPage(String schoolName, Integer status, Date startDate, Date endDate, Integer pageNo, Integer pageSize) {

        Pageable page = new PageRequest(pageNo, pageSize, Sort.Direction.DESC, "createTime");

        Specification<SchoolDrawRecord> specification = (root, query, cb) -> {
            List<Predicate> lstPredicates = new ArrayList<Predicate>();
            if (StringUtils.isNotBlank(schoolName)) {
                lstPredicates.add(cb.like(root.get("schoolName"), "%" + schoolName + "%"));
            }
            if (status != null)
                lstPredicates.add(cb.equal(root.get("status"), status));
            if (startDate != null && endDate != null) {
                lstPredicates.add(cb.between(root.get("createTime"), startDate, endDate));
            }
            Predicate[] arrayPredicates = new Predicate[lstPredicates.size()];
            return cb.and(lstPredicates.toArray(arrayPredicates));
        };

        return schoolDrawRecordRepository.findAll(specification, page);
    }

    @Override
    public BigDecimal getSum(Date startDate, Date endDate) {
        return schoolDrawRecordRepository.getSum(startDate, endDate);
    }
}
