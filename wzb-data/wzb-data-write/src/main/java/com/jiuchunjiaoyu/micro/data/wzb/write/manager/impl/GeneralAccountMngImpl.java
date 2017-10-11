package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.GeneralAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.GeneralAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class GeneralAccountMngImpl extends BaseMngImpl<GeneralAccount> implements GeneralAccountMng {

    @Resource
    private GeneralAccountRepository generalAccountRepository;

    @Override
    protected CrudRepository<GeneralAccount, Long> getRepository() {
        return generalAccountRepository;
    }

    @Override
    public GeneralAccount getGeneralAccount() {
        List<GeneralAccount> generalAccounts = generalAccountRepository.findAll();
        if (generalAccounts == null || generalAccounts.isEmpty())
            return null;
        return generalAccounts.get(0);
    }

    @Override
    public GeneralAccount addSchoolAmount(BigDecimal amount) {
        GeneralAccount generalAccount = getGeneralAccount();
        BigDecimal schoolAmount = generalAccount.getSchoolAmount();
        if (schoolAmount == null)
            schoolAmount = new BigDecimal(0);
        generalAccount.setSchoolAmount(schoolAmount.add(amount));

        return generalAccountRepository.save(generalAccount);
    }

}
