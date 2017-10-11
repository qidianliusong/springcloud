package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;

public interface FeeAgentMng extends BaseMng<FeeAgent>{

	Page<FeeAgent> getPageInfo(Long classId,int pageNo,int pageSize);

	List<FeeAgent> getList(Long feeDetailId);

    List<FeeAgent> getPreFeeAgents(Date startTime);
}
