package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.GeneralAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.GeneralAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.GeneralAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GeneralAccountMngImpl extends BaseMngImpl<GeneralAccount> implements GeneralAccountMng {

    @Resource
    private GeneralAccountRepository generalAccountRepository;

    @Override
    protected JpaRepository<GeneralAccount, Long> getRepository() {
        return generalAccountRepository;
    }

    @Override
    public GeneralAccount getGeneralAccount() {
        List<GeneralAccount> generalAccounts = generalAccountRepository.findAll();
        if (generalAccounts == null || generalAccounts.isEmpty())
            return null;
        return generalAccounts.get(0);
    }

}
