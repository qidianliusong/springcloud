package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDrawRecord;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeeDrawRecordMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeeDrawRecordRepository;

import java.util.Date;
import java.util.List;

@Service
public class FeeDrawRecordMngImpl extends BaseMngImpl<FeeDrawRecord> implements FeeDrawRecordMng{

	@Resource
	private FeeDrawRecordRepository feeDrawRecordRepository;
	
	@Override
	protected JpaRepository<FeeDrawRecord, Long> getRepository() {
		return feeDrawRecordRepository;
	}

	@Override
	public Page<FeeDrawRecord> getPage(Long classId, Integer[] status, Integer pageNo, Integer pageSize) {
		Pageable page = new PageRequest(pageNo, pageSize);
		if(status == null || status.length == 0)
			return feeDrawRecordRepository.getPage(classId,page);
		if(status.length == 1)
			return feeDrawRecordRepository.getPage(classId,status[0],page);
		return feeDrawRecordRepository.getPage(classId,status,page);
	}

	@Override
	public List<FeeDrawRecord> getListByStatus(Integer status, Date startDate) {
		return feeDrawRecordRepository.getListByStatus(status,startDate);
	}

	@Override
	public int countByStatus(Integer status, Date startDate, Long classId) {
		return feeDrawRecordRepository.countByStatus(status, startDate,classId);
	}

}
