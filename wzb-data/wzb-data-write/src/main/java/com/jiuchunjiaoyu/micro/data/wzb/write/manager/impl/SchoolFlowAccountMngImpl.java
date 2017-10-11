package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolFlowAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.SchoolFlowAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolFlowAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class SchoolFlowAccountMngImpl extends BaseMngImpl<SchoolFlowAccount> implements SchoolFlowAccountMng {

    @Resource
    private SchoolFlowAccountRepository schoolFlowAccountRepository;

    @Override
    protected CrudRepository<SchoolFlowAccount, Long> getRepository() {
        return schoolFlowAccountRepository;
    }
}
