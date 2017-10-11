package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import java.util.List;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;

/**
 * 交班费记录批处理接口 
 *
 */
public interface FeePayBatchRepository {

	void batchSave(List<FeePay> feePays);
	
}
