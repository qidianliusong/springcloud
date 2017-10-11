package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;
import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeeTakeMng;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.ClassAccountRepository;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeeTakeRepository;
/**
 * 记账管理
 */
@Service
@Transactional
public class FeeTakeMngImpl extends BaseMngImpl<FeeTake> implements FeeTakeMng{

	@Resource
	private FeeTakeRepository feeTakeRepository;
	@Resource
	private ClassAccountRepository classAccountRepository;
	
	@Override
	protected CrudRepository<FeeTake, Long> getRepository() {
		return feeTakeRepository;
	}

	@Override
	public FeeTake saveFeeTake(FeeTake feeTake) {
		if(feeTake.getFeeTakeId() == null)
			feeTake.setCreateTime(new Date());
		save(feeTake);
		updateAccountAmount(feeTake);
		return feeTake;
	}

	private void updateAccountAmount(FeeTake feeTake) {
		BigDecimal sumAmount = feeTakeRepository.getSumAmount(feeTake.getClassId());
		ClassAccount classAccount = classAccountRepository.findByClassId(feeTake.getClassId());
		classAccount.setAccountingAmount(sumAmount);
		classAccountRepository.save(classAccount);
	}

	@Override
	public void deleteFeeTake(FeeTake feeTake) {
		this.delete(feeTake);
		updateAccountAmount(feeTake);
	}

}
