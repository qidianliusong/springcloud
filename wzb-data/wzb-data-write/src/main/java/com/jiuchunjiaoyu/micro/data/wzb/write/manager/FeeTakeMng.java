package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeTake;

/**
 *  代交班费管理器
 *
 */
public interface FeeTakeMng extends BaseMng<FeeTake>{

	FeeTake saveFeeTake(FeeTake feeTake);

	void deleteFeeTake(FeeTake feeTake);
}
