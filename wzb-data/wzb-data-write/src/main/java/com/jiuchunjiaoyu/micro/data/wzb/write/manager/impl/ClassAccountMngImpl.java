package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.ClassAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.ClassAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
@Transactional
public class ClassAccountMngImpl extends BaseMngImpl<ClassAccount> implements ClassAccountMng {

    @Resource
    private ClassAccountRepository classAccountRepository;

    @Override
    protected CrudRepository<ClassAccount, Long> getRepository() {
        return classAccountRepository;
    }
}
