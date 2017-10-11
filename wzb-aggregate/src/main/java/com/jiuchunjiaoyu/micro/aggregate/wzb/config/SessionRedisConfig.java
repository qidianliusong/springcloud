package com.jiuchunjiaoyu.micro.aggregate.wzb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;
//@Configuration
//@EnableAutoConfiguration
public class SessionRedisConfig {
	public static Logger logger = LoggerFactory.getLogger(SessionRedisConfig.class);
	
	@Bean
    @ConfigurationProperties(prefix="spring.redis.session")  
    public JedisPoolConfig getSessiontRedisConfig(){  
        JedisPoolConfig config = new JedisPoolConfig();  
        return config;  
    }  
      
    @Bean
    @ConfigurationProperties(prefix="spring.redis.session")
    public JedisConnectionFactory getSessionConnectionFactory(){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        JedisPoolConfig config = getSessiontRedisConfig();  
        factory.setPoolConfig(config);
        logger.info("session用redis连接池配置成功");
        return factory;  
    }  
      
      
    @Bean(name="phpSessionRedisTemplate",autowire=Autowire.BY_NAME)
    public RedisTemplate<String, Object> getSessionRedisTemplate(){
    	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    	redisTemplate.setConnectionFactory(getSessionConnectionFactory());
        return redisTemplate;
    }
    
    @Bean(name="phpSessionStringRedisTemplate",autowire=Autowire.BY_NAME)
    public StringRedisTemplate getSessionStringRedisTemplate(){
        return new StringRedisTemplate(getSessionConnectionFactory());  
    }
}
