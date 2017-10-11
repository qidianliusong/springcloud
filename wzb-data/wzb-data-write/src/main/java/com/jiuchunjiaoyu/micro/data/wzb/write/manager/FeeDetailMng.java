package com.jiuchunjiaoyu.micro.data.wzb.write.manager;
/**
 * 收费详情相关 
 *
 */

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeDetail;

public interface FeeDetailMng extends BaseMng<FeeDetail>{

	FeeDetail saveFeeDetail(FeeDetail feeDetail);
//	
//	FeeDetail updateFeeDetail(FeeDetail feeDetail);
//	
//	void deleteFeeDetailById(Long feeDetailId);
	
}
