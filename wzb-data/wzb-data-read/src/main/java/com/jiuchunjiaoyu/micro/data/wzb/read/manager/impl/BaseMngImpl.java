package com.jiuchunjiaoyu.micro.data.wzb.read.manager.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jiuchunjiaoyu.micro.data.wzb.read.manager.BaseMng;

public abstract class BaseMngImpl<T> implements BaseMng<T> {

	@Override
	public boolean exists(Long id){
		return getRepository().exists(id);
	}
	
	@Override
	public T findOne(Long id) {
		return getRepository().findOne(id);
	}

	protected abstract JpaRepository<T, Long> getRepository();
	

}
