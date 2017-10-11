package com.jiuchunjiaoyu.micro.data.wzb.write.manager;

public interface BaseMng<T> {
	
	T save(T entity);
	T update(T entity);
	void delete(Long id);
	void delete(T entity);
	T findOne(Long id);
	boolean exists(Long id);
}
