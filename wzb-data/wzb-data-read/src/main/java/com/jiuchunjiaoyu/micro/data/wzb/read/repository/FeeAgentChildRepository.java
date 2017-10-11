package com.jiuchunjiaoyu.micro.data.wzb.read.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiuchunjiaoyu.micro.data.wzb.common.entity.FeeAgentChild;

@Repository
public interface FeeAgentChildRepository extends JpaRepository<FeeAgentChild, Long>{

}
