package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.TestUserEntity;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.TestUserMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.TestUserRepository;

@Service
@Transactional
public class TestUserMngImpl implements TestUserMng{

	@Resource
	private TestUserRepository testUserRepository;
	
	@Override
	public TestUserEntity save(TestUserEntity user) {
		return testUserRepository.save(user);
	}

}
