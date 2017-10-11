package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import java.util.List;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;

/**
 * 收费类目manager 
 *
 */
public interface FeePayMng extends BaseMng<FeePay>{

	void batchSave(List<FeePay> feePays);

	void pay(FeePay feePay) throws WzbDataException;

	void afterPay(Long feePayId) throws WzbDataException;
	
}
