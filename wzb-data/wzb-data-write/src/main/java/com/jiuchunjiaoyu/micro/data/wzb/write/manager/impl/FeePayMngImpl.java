package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.*;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;
import com.jiuchunjiaoyu.micro.data.wzb.write.constant.WzbDataWriteErr;
import com.jiuchunjiaoyu.micro.data.wzb.write.manager.FeePayMng;

@Service
@Transactional
public class FeePayMngImpl extends BaseMngImpl<FeePay> implements FeePayMng {

	@Resource
	private FeePayRepository feePayRepository;

	@Resource
	private FeePayBatchRepository feePayBatchRepository;

	@Resource
	private FeeDetailRepository feeDetailRepository;
	
	@Resource
	private ClassAccountRepository classAccountRepository;

	@Resource
    private SchoolAccountRepository schoolAccountRepository;

	@Override
	protected CrudRepository<FeePay, Long> getRepository() {
		return feePayRepository;
	}

	@Override
	public void batchSave(List<FeePay> feePays) {
		feePayBatchRepository.batchSave(feePays);
	}

	@Override
	public void pay(FeePay feePay) throws WzbDataException {
		
	}

	@Override
	public void afterPay(Long feePayId) throws WzbDataException {
		FeePay feePay = feePayRepository.findOne(feePayId);
		FeeDetail feeDetail = feePay.getFeeDetail();
		if (feeDetail == null) {
			throw new WzbDataException(WzbDataWriteErr.fee_detail_not_exists.getCode(),
					WzbDataWriteErr.fee_detail_not_exists.getMessage());
		}
		ClassAccount classAccount = classAccountRepository.findByClassId(feeDetail.getClassId());
		if(classAccount == null){
			throw new WzbDataException(WzbDataWriteErr.classaccount_not_exists.getCode(),
					WzbDataWriteErr.classaccount_not_exists.getMessage());
		}
		Date now = new Date();
		feePay.setPayTime(now);
		feePay.setStatus(FeePay.STATUS_PAY);
		feePay.setPayTime(new Date());
		feePayRepository.save(feePay);
		
		feeDetail.setPaidCount(feeDetail.getPaidCount()+1);

		if(feeDetail.getTotalAmount() == null ){
			feeDetail.setTotalAmount(new BigDecimal(0));
		}
		feeDetail.setTotalAmount(feeDetail.getTotalAmount().add(feeDetail.getAmount()));
		feeDetailRepository.save(feeDetail);
		FeeCategory feeCategory = feeDetail.getFeeCategory();
		//处理学校后台配置的固定收费类目，学校后台的收费不计入班费，直接计入学校账户
		if(feeCategory!=null && feeCategory.getType() == FeeCategory.TYPE_BACKGROUND){
            SchoolAccount schoolAccount = schoolAccountRepository.findBySchoolId(classAccount.getSchoolId());
            if(schoolAccount.getSchoolAmount() == null)
                schoolAccount.setSchoolAmount(new BigDecimal(0));
            schoolAccount.setSchoolAmount(schoolAccount.getSchoolAmount().add(feeDetail.getAmount()));
            schoolAccountRepository.save(schoolAccount);
            return;
        }

        //处理普通类别的收费
		classAccount.setAmount(classAccount.getAmount().add(feeDetail.getAmount()));
		
		classAccountRepository.save(classAccount);
	}

}
