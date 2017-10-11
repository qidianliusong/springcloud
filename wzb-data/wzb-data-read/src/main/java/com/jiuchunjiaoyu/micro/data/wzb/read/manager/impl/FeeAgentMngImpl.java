package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeAgentMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeeAgentRepository;

@Service
public class FeeAgentMngImpl extends BaseMngImpl<FeeAgent> implements FeeAgentMng{

	@Resource
	private FeeAgentRepository feeAgentRepository;
		
	@Override
	protected JpaRepository<FeeAgent, Long> getRepository() {
		return feeAgentRepository;
	}

	@Override
	public Page<FeeAgent> getPageInfo(Long classId, int pageNo, int pageSize) {
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return feeAgentRepository.getPageInfo(classId,FeeAgent.STATUS_PAY,pageable);
	}

	@Override
	public List<FeeAgent> getList(Long feeDetailId) {
		return feeAgentRepository.findByFeeDetailId(feeDetailId);
	}

	@Override
	public List<FeeAgent> getPreFeeAgents(Date startTime) {
		return feeAgentRepository.findByStatus(FeeAgent.STATUS_PRE_PAY,startTime);
	}

}
