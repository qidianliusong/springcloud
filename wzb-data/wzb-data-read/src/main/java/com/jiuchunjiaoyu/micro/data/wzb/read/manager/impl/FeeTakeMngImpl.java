package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeTakeMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeeTakeRepository;
/**
 *记账manager 
 *
 */
@Service
public class FeeTakeMngImpl extends BaseMngImpl<FeeTake> implements FeeTakeMng{
	
	@Resource
	private FeeTakeRepository feeTakeRepository;

	@Override
	public Page<FeeTake> getPageInfo(int pageNo, int pageSize,Long classId) {
		PageRequest pageRequest = new PageRequest(pageNo-1, pageSize,new Sort(Direction.DESC, "createTime"));
		return feeTakeRepository.findByClassId(classId, pageRequest);
	}

	@Override
	protected JpaRepository<FeeTake, Long> getRepository() {
		return feeTakeRepository;
	}

}
