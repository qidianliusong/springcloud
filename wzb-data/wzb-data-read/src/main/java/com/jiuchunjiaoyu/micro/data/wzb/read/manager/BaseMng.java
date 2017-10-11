package com.jiuchunjiaoyu.micro.data.wzb.read.manager;

public interface BaseMng<T> {
	
	T findOne(Long id);
	boolean exists(Long id);
}
