package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.AccountFlow;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ProvinceFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.AccountFlowMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.AccountFlowRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.GeneralAccountRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.ProvinceFlowAccountRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolFlowAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountFlowMngImpl extends BaseMngImpl<AccountFlow> implements AccountFlowMng {

    @Resource
    private AccountFlowRepository accountFlowRepository;
    @Resource
    private SchoolFlowAccountRepository schoolFlowAccountRepository;
    @Resource
    private GeneralAccountRepository generalAccountRepository;
    @Resource
    private ProvinceFlowAccountRepository provinceFlowAccountRepository;

    @Override
    protected CrudRepository<AccountFlow, Long> getRepository() {
        return accountFlowRepository;
    }

    @Override
    public AccountFlow saveAccountFlow(AccountFlow accountFlow) {
        AccountFlow save = this.save(accountFlow);
        SchoolFlowAccount schoolFlowAccount = schoolFlowAccountRepository.findBySchoolId(accountFlow.getSchoolId());
        if (schoolFlowAccount == null){
            schoolFlowAccount = new SchoolFlowAccount();
            schoolFlowAccount.setSchoolName(accountFlow.getSchoolName());
            schoolFlowAccount.setProvince(accountFlow.getProvince());
            schoolFlowAccount.setAmount(new BigDecimal(0));
            schoolFlowAccount.setCity(accountFlow.getCity());
            schoolFlowAccount.setSchoolId(accountFlow.getSchoolId());
            schoolFlowAccount.setTown(accountFlow.getTown());
            schoolFlowAccount.setCounty(accountFlow.getCounty());
        }
        schoolFlowAccount.setAmount(schoolFlowAccount.getAmount().add(save.getAmount()));
        schoolFlowAccountRepository.save(schoolFlowAccount);
        List<GeneralAccount> list = generalAccountRepository.findAll();
        GeneralAccount generalAccount;
        if (list == null || list.isEmpty()) {
            generalAccount = new GeneralAccount();
            generalAccount.setAmount(new BigDecimal(0));
        } else {
            generalAccount = list.get(0);
        }
        generalAccount.setAmount(generalAccount.getAmount().add(accountFlow.getAmount()));
        generalAccountRepository.save(generalAccount);

        ProvinceFlowAccount provinceFlowAccount = provinceFlowAccountRepository.findByProvinceName(schoolFlowAccount.getProvince());
        if (provinceFlowAccount == null) {
            ProvinceFlowAccount p = new ProvinceFlowAccount();
            p.setAmount(schoolFlowAccount.getAmount());
            p.setProvinceName(schoolFlowAccount.getProvince());
            provinceFlowAccountRepository.save(p);
        } else {
            provinceFlowAccount.setAmount(provinceFlowAccount.getAmount().add(generalAccount.getAmount()));
            provinceFlowAccountRepository.save(provinceFlowAccount);
        }

        return save;
    }

    @Override
    public boolean existByTradeNo(String tradeNo) {
        return accountFlowRepository.existByTradeNo(tradeNo);
    }
}
