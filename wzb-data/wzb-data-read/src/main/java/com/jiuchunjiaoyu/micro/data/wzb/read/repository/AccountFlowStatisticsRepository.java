package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public interface AccountFlowStatisticsRepository {

    BigDecimal getStatistics(String province, Long schoolId, Long classId,Integer type ,Date startDate, Date endDate);
}
