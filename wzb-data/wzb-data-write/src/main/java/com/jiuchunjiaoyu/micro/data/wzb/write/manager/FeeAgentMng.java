package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;
import com.jiuchunjiaoyu.micro.data.wzb.common.exception.WzbDataException;

/**
 *  代交班费管理器
 *
 */
public interface FeeAgentMng extends BaseMng<FeeAgent>{

	void afterPay(Long feeAgentId) throws WzbDataException;
	
	FeeAgent saveFeeAgent(FeeAgent feeAgent);
	
}
