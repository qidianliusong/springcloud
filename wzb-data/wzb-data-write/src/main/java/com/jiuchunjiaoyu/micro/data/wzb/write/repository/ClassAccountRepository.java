package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.ClassAccount;

@Repository
public interface ClassAccountRepository extends CrudRepository<ClassAccount, Long>{

	ClassAccount findByClassId(Long classId);
	
}
