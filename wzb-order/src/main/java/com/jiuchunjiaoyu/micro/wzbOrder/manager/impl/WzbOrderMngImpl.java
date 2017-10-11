package com.jiuchunjiaoyu.micro.wzbOrder.manager.impl;

import com.jiuchunjiaoyu.micro.wzbOrder.entity.WzbOrder;
import com.jiuchunjiaoyu.micro.wzbOrder.manager.WzbOrderMng;
import com.jiuchunjiaoyu.micro.wzbOrder.repository.WzbOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class WzbOrderMngImpl implements WzbOrderMng {

	private static Logger logger = LoggerFactory.getLogger(WzbOrderMngImpl.class);

	@Resource
	private WzbOrderRepository wzbOrderRepository;

	@Override
	@Transactional
	public WzbOrder saveOrUpdate(WzbOrder wzbOrder) {
		return wzbOrderRepository.save(wzbOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public WzbOrder findById(Long wzbOrderId) {
		return wzbOrderRepository.findOne(wzbOrderId);
	}

	@Override
	@Transactional(readOnly = true)
	public WzbOrder findByOutTradeNo(String outTradeNo) {
		return wzbOrderRepository.findByOutTradeNo(outTradeNo);
	}
}
