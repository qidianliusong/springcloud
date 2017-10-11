package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.ClassAccountMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.ClassAccountRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClassAccountMngImpl extends BaseMngImpl<ClassAccount> implements ClassAccountMng {

    @Resource
    private ClassAccountRepository classAccountRepository;

    @Override
    protected JpaRepository<ClassAccount, Long> getRepository() {
        return classAccountRepository;
    }

    @Override
    public ClassAccount findByClassId(Long classId) {
        return classAccountRepository.findByClassId(classId);
    }

    @Override
    public boolean existsByClassId(Long classId) {
        return classAccountRepository.existsByClassId(classId);
    }

    @Override
    public Page<ClassAccount> getClassAccountPage(Long schoolId, String className, Integer pageNo, Integer pageSize) {
        Pageable page = new PageRequest(pageNo, pageSize);

        ClassAccount classAccount = new ClassAccount();
        classAccount.setSchoolId(schoolId);
        if (StringUtils.isNotBlank(className))
            classAccount.setClassName(className);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("className", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING))
                .withIgnorePaths("version");
        Example<ClassAccount> example = Example.of(classAccount, matcher);
        return classAccountRepository.findAll(example, page);
    }

}
