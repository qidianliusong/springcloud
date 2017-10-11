package com.jiuchunjiaoyu.micro.data.wzb.write.manager.impl;

import org.springframework.data.repository.CrudRepository;

import com.jiuchunjiaoyu.micro.data.wzb.write.manager.BaseMng;

public abstract class BaseMngImpl<T> implements BaseMng<T> {

	
	@Override
	public T save(T entity) {
		return getRepository().save(entity);
	}

	@Override
	public T update(T entity) {
		return getRepository().save(entity);
	}

	@Override
	public void delete(Long id) {
		getRepository().delete(id);
	}

    @Override
    public void delete(T entity) {
		getRepository().delete(entity);
    }

    @Override
	public boolean exists(Long id){
		return getRepository().exists(id);
	}
	
	@Override
	public T findOne(Long id) {
		return getRepository().findOne(id);
	}

	protected abstract CrudRepository<T, Long> getRepository();
	

}
