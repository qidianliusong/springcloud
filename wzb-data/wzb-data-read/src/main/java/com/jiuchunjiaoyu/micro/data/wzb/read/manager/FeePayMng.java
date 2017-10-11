package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import java.util.Date;
import java.util.List;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;

public interface FeePayMng extends BaseMng<FeePay>{

	List<FeePay> findByFeeDetailId(Long feeDetailId, Integer... payStatus);

	FeePay findByFeeDetailIdAndChildId(Long feeDetailId, Long childId);

    List<FeePay> getPreFeePays(Date startTime);
}
