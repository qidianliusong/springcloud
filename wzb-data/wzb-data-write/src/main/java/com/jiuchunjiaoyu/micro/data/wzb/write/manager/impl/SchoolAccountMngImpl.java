package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.SchoolAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class SchoolAccountMngImpl extends BaseMngImpl<SchoolAccount> implements SchoolAccountMng {

    @Resource
    private SchoolAccountRepository schoolAccountRepository;

    @Override
    protected CrudRepository<SchoolAccount, Long> getRepository() {
        return schoolAccountRepository;
    }

}
