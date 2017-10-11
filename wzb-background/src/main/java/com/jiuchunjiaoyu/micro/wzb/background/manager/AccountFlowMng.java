package com.jiuchunjiaoyu.micro.wzb.background.manager;

import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountFlowMng {

    Map<String, BigDecimal> getStatisticsUseCache(String province, Long schoolId, Long classId, String startDate, String endDate) throws WzbBackgroundException;

    Map<String, BigDecimal> getStatistics(String province, Long schoolId, Long classId, String startDate, String endDate) throws WzbBackgroundException;

}
