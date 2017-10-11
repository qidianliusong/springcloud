package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ProvinceFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.AccountFlowMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.*;
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
public class AccountFlowMngImpl extends BaseMngImpl<AccountFlow> implements AccountFlowMng {

    @Resource
    private AccountFlowRepository accountFlowRepository;

    @Resource
    private AccountFlowStatisticsRepository accountFlowStatisticsRepository;

    @Resource
    private GeneralAccountRepository generalAccountRepository;

    @Resource
    private SchoolFlowAccountRepository schoolFlowAccountRepository;

    @Resource
    private ProvinceFlowAccountRepository provinceFlowAccountRepository;

    @Override
    protected JpaRepository<AccountFlow, Long> getRepository() {
        return accountFlowRepository;
    }

    @Override
    public Page<AccountFlow> getPage(String province, Long schoolId, Long classId, Integer type, Date startDate, Date endDate, Integer pageNo, Integer pageSize) {

        Pageable page = new PageRequest(pageNo, pageSize, new Sort(Sort.Direction.DESC, "flowTime"));

        Specification<AccountFlow> specification = (root, query, cb) -> {
            List<Predicate> lstPredicates = new ArrayList<>();
            if (type != null)
                lstPredicates.add(cb.equal(root.get("type"), type));
            if (schoolId != null) {
                lstPredicates.add(cb.equal(root.get("schoolId"), schoolId));
            }
            if (province != null) {
                lstPredicates.add(cb.equal(root.get("province"), province));
            }
            if (classId != null)
                lstPredicates.add(cb.equal(root.get("classId"), classId));
            if (startDate != null && endDate != null) {
                lstPredicates.add(cb.between(root.get("flowTime"), startDate, endDate));
            }
            Predicate[] arrayPredicates = new Predicate[lstPredicates.size()];
            return cb.and(lstPredicates.toArray(arrayPredicates));
        };
        return accountFlowRepository.findAll(specification, page);
    }

    @Override
    public BigDecimal getStatistics(String province, Long schoolId, Long classId, Integer type, Date startDate, Date endDate) {
        return accountFlowStatisticsRepository.getStatistics(province, schoolId, classId, type, startDate, endDate);
    }

    @Override
    public GeneralAccount getGeneralAccount() {
        List<GeneralAccount> generalAccounts = generalAccountRepository.findAll();
        if (generalAccounts == null || generalAccounts.isEmpty())
            return null;
        return generalAccounts.get(0);
    }

    @Override
    public SchoolFlowAccount getSchoolFlowAccount(Long schoolId) {
        return schoolFlowAccountRepository.findBySchoolId(schoolId);
    }

    @Override
    public ProvinceFlowAccount getProvinceFlowAccount(String provinceName) {
        return provinceFlowAccountRepository.findByProvinceName(provinceName);
    }

    @Override
    public List<ProvinceFlowAccount> getProvinceFlowAccountList() {
        Sort sort = new Sort(Sort.Direction.DESC, "provinceFlowAccountId");
        return provinceFlowAccountRepository.findAll(sort);
    }

    @Override
    public Page<SchoolFlowAccount> getSchoolFlowAccountPage(String province, String schoolName, Integer pageNo, Integer pageSize) {

        Pageable page = new PageRequest(pageNo, pageSize);

        SchoolFlowAccount schoolFlowAccount = new SchoolFlowAccount();
        if (StringUtils.isNotBlank(province))
            schoolFlowAccount.setProvince(province);
        if (StringUtils.isNotBlank(schoolName))
            schoolFlowAccount.setSchoolName(schoolName);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("schoolName", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));

        Example<SchoolFlowAccount> example = Example.of(schoolFlowAccount, matcher);

        return schoolFlowAccountRepository.findAll(example, page);
    }
}
