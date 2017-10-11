package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface FeeDrawRecordMng extends BaseMng<FeeDrawRecord> {

    Page<FeeDrawRecord> getPage(Long classId, Integer[] status, Integer pageNo, Integer pageSize);

    List<FeeDrawRecord> getListByStatus(Integer status, Date startDate);

    int countByStatus(Integer status, Date startDate, Long classId);

}
