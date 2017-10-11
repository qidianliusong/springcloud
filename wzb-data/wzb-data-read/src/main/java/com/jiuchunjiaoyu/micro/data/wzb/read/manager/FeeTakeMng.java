package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

import org.springframework.data.domain.Page;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;

public interface FeeTakeMng extends BaseMng<FeeTake>{

	Page<FeeTake> getPageInfo(int pageNo,int pageSize,Long classId);

}
