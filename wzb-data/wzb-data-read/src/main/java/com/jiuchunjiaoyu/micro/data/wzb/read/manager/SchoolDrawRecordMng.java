package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolDrawRecord;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;

public interface SchoolDrawRecordMng extends BaseMng<SchoolDrawRecord> {

    Page<SchoolDrawRecord> getPageBySchoolId(Long schoolId, Integer status, Integer pageNo, Integer pageSize);

    Page<SchoolDrawRecord> getPageBySchoolId(Long schoolId, Integer status, Date startDate, Date endDate, Integer pageNo, Integer pageSize);

    Page<SchoolDrawRecord> getPage(String schoolName, Integer status, Date startDate, Date endDate, Integer pageNo, Integer pageSize);

    BigDecimal getSum(Date startDate, Date endDate);
}
