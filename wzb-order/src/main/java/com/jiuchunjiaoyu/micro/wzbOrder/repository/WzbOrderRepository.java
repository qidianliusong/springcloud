package com.jiuchunjiaoyu.micro.wzbOrder.repository;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.WzbOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WzbOrderRepository extends JpaRepository<WzbOrder, Long>{

	WzbOrder findByOutTradeNo(String outTradeNo);

}
