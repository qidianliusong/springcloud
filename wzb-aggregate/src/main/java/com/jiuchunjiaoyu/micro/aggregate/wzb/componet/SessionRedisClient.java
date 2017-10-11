package com.jiuchunjiaoyu.micro.aggregate.wzb.componet;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.jiuchunjiaoyu.micro.base.common.redis.RedisClient;

//@Component
public class SessionRedisClient extends RedisClient{

	@Resource(name="phpSessionStringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;

	@Resource(name="phpSessionRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	@Override
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}
	
	
	
}
