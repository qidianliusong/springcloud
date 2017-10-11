package com.jiuchunjiaoyu.micro.data.wzb.write.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgent;

@Repository
public interface FeeAgentRepository extends CrudRepository<FeeAgent, Long>{

}
