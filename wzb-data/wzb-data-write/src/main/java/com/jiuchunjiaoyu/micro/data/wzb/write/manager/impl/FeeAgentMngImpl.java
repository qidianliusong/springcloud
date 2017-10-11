package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.*;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeDetailRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.SchoolAccountRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeAgentMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.ClassAccountRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeAgentRepository;

@Service
@Transactional
public class FeeAgentMngImpl extends BaseMngImpl<FeeAgent> implements FeeAgentMng {

	@Resource
	private FeeAgentRepository feeAgentRepository;
	
	@Resource
	private ClassAccountRepository classAccountRepository;

	@Resource
    private FeeDetailRepository feeDetailRepository;

	@Resource
	private SchoolAccountRepository schoolAccountRepository;

	@Override
	public void afterPay(Long feeAgentId) throws WzbDataException {
		FeeAgent feeAgent = this.findOne(feeAgentId);
		feeAgent.setStatus(FeePay.STATUS_PAY);
		feeAgent.setPayTime(new Date());
		FeeDetail feeDetail = feeAgent.getFeeDetail();
		ClassAccount classAccount = classAccountRepository.findByClassId(feeDetail.getClassId());
		if(classAccount == null){
			throw new WzbDataException(WzbDataWriteErr.classaccount_not_exists.getCode(),WzbDataWriteErr.classaccount_not_exists.getMessage());
		}

		if(feeDetail.getTotalAmount() == null)
			feeDetail.setTotalAmount(new BigDecimal(0));
		feeDetail.setTotalAmount(feeDetail.getTotalAmount().add(feeAgent.getTotalAmount()));

		feeDetailRepository.save(feeDetail);

		FeeCategory feeCategory = feeDetail.getFeeCategory();
		//处理学校后台配置的固定收费类目，学校后台的收费不计入班费，直接计入学校账户
		if(feeCategory!= null && feeCategory.getType() == FeeCategory.TYPE_BACKGROUND){
			SchoolAccount schoolAccount = schoolAccountRepository.findBySchoolId(classAccount.getSchoolId());
			if(schoolAccount.getSchoolAmount() == null)
				schoolAccount.setSchoolAmount(new BigDecimal(0));
			schoolAccount.setSchoolAmount(schoolAccount.getSchoolAmount().add(feeAgent.getTotalAmount()));
			schoolAccountRepository.save(schoolAccount);
			return;
		}
		classAccount.setAmount(classAccount.getAmount().add(feeAgent.getTotalAmount()));
		classAccountRepository.save(classAccount);
	}

	@Override
	protected CrudRepository<FeeAgent, Long> getRepository() {
		return feeAgentRepository;
	}

	@Override
	public FeeAgent saveFeeAgent(FeeAgent feeAgent) {
		if(feeAgent.getFeeAgentId() != null){
			FeeAgent findOne = findOne(feeAgent.getFeeAgentId());
			if(findOne != null && findOne.getFeeAgentChilds() != null){
				findOne.getFeeAgentChilds().clear();
				save(findOne);
			}
		}
		return save(feeAgent);
	}

}
