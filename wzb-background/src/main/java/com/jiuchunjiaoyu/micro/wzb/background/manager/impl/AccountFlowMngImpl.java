package com.jiuchunjiaoyu.micro.wzb.background.manager.impl;

import com.jiuchunjiaoyu.micro.base.common.component.result.BaseResult;
import com.jiuchunjiaoyu.micro.wzb.background.exception.WzbBackgroundException;
import com.jiuchunjiaoyu.micro.wzb.background.feign.WzbDataReadFeign;
import com.jiuchunjiaoyu.micro.wzb.background.manager.AccountFlowMng;
import com.jiuchunjiaoyu.micro.wzb.background.util.FeignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountFlowMngImpl implements AccountFlowMng {

    private static Logger logger = LoggerFactory.getLogger(AccountFlowMngImpl.class);

    @Resource
    private WzbDataReadFeign readFeign;

    @Override
    @Cacheable(value = "wzb:background", key = "'wzb:background:getStatisticsUseCache_key_'+#p0+'_'+#p1+'_'+#p2+'_'+#p3+'_'+#p4")
    public Map<String, BigDecimal> getStatisticsUseCache(String province, Long schoolId, Long classId, String startDate, String endDate) throws WzbBackgroundException {
        return getStatistics(province, schoolId, classId, startDate, endDate);
    }

    @Override
    public Map<String, BigDecimal> getStatistics(String province, Long schoolId, Long classId, String startDate, String endDate) throws WzbBackgroundException {
        BaseResult<Map<String, BigDecimal>> accountFlowStatistics = readFeign.getAccountFlowStatistics(province, schoolId, classId, startDate, endDate);
        FeignUtil.validateFeignResult(accountFlowStatistics, "readFeign.getAccountFlowStatistics", logger);
        return accountFlowStatistics.getData();
    }
}
