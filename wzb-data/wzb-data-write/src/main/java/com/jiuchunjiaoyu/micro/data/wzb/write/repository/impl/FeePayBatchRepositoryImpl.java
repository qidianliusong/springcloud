package com.jiuchunjiaoyu.micro.data.wzb.write.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeePay;
import com.jiuchunjiaoyu.micro.data.wzb.write.repository.FeePayBatchRepository;

@Repository
public class FeePayBatchRepositoryImpl implements FeePayBatchRepository {
	
	private static final int BATCH_SIZE = 30;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void batchSave(List<FeePay> feePays) {
		if(feePays == null || feePays.isEmpty())
			return;
		int count = 0;
		for (FeePay feePay : feePays) {
			em.persist(feePay);
			if(++count % BATCH_SIZE == 0){
				em.flush();
				em.clear();
			}
		}
	}

}
