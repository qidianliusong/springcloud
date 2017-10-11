package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.SchoolAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.SchoolAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.SchoolAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SchoolAccountMngImpl extends BaseMngImpl<SchoolAccount> implements SchoolAccountMng {

    @Resource
    private SchoolAccountRepository schoolAccountRepository;

    @Override
    protected JpaRepository<SchoolAccount, Long> getRepository() {
        return schoolAccountRepository;
    }


    @Override
    public SchoolAccount findBySchoolId(Long schoolId) {
        return schoolAccountRepository.findBySchoolId(schoolId);
    }

    @Override
    public boolean existsBySchoolId(Long schoolId) {
        return schoolAccountRepository.existsBySchoolId(schoolId);
    }
}
