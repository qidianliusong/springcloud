package com.jiuchunjiaoyu.micro.wzbOrder.manager;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.WzbOrder;

public interface WzbOrderMng {

	WzbOrder saveOrUpdate(WzbOrder wzbOrder);

	WzbOrder findById(Long wzbOrderId);

	WzbOrder findByOutTradeNo(String outTradeNo);

}
