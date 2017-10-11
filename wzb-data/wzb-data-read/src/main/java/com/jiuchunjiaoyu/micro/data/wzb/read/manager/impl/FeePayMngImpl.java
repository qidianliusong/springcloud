package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.read.manager.FeePayMng;
import com.jiuchunjiaoyu.micro.data.wzb.read.repository.FeePayRepository;

@Service
public class FeePayMngImpl extends BaseMngImpl<FeePay> implements FeePayMng{

	@Resource
	private FeePayRepository feePayRepository;
		
	@Override
	protected JpaRepository<FeePay, Long> getRepository() {
		return feePayRepository;
	}

	@Override
	public List<FeePay> findByFeeDetailId(Long feeDetailId, Integer... payStatus) {
		if(payStatus == null || payStatus.length == 0 ){
			return feePayRepository.findByFeeDetailId(feeDetailId);
		}
		return feePayRepository.findByFeeDetailId(feeDetailId,payStatus);
	}

	@Override
	public FeePay findByFeeDetailIdAndChildId(Long feeDetailId, Long childId) {
		return feePayRepository.findByFeeDetailIdAndChildId(feeDetailId,childId);
	}

    @Override
    public List<FeePay> getPreFeePays(Date startTime) {
        return feePayRepository.findByStatus(FeePay.STATUS_PRE_PAY,startTime);
    }

}
