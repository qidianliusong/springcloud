package com.jiuchunjiaoyu.micro.data.wzb.read.repository.impl;

import com.jiuchunjiaoyu.micro.data.wzb.read.repository.AccountFlowStatisticsRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AccountFlowStatisticsRepositoryImpl implements AccountFlowStatisticsRepository {

    @PersistenceContext
    private EntityManager em;

    public BigDecimal getStatistics(String province, Long schoolId, Long classId, Integer type, Date startDate, Date endDate) {
        StringBuilder sqlBuilder = new StringBuilder("select sum(amount) from AccountFlow where 1=1 ");
        Map<String, Object> paramMap = new HashMap<>();
        if (type != null) {
            sqlBuilder.append("and type=:type ");
            paramMap.put("type", type);
        }
        if (!StringUtils.isBlank(province)) {
            sqlBuilder.append("and province=:province ");
            paramMap.put("province", province);
        }
        if (schoolId != null) {
            sqlBuilder.append("and schoolId=:schoolId ");
            paramMap.put("schoolId", schoolId);
        }
        if (classId != null) {
            sqlBuilder.append("and classId=:classId ");
            paramMap.put("classId", classId);
        }
        if (startDate != null && endDate != null) {
            sqlBuilder.append("and flowTime between :startDate and :endDate ");
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
        }
        Query query = em.createQuery(sqlBuilder.toString());

        if (!paramMap.isEmpty()) {
            paramMap.entrySet().stream().forEach(entry -> {
                if (entry == null || entry.getKey() == null)
                    return;
                query.setParameter(entry.getKey(), entry.getValue());
            });
        }
        return (BigDecimal) query.getSingleResult();
    }
}
