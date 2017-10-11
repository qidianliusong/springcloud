package com.jiuchunjiaoyu.micro.data.wzb.read;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 微智宝可读数据服务启动类
 *
 */
@SpringBootApplication(scanBasePackages={"com.jiuchunjiaoyu.micro.data.wzb.read","com.jiuchunjiaoyu.micro.data.wzb.common","com.jiuchunjiaoyu.micro.base.common.redis"})
@EntityScan(basePackages={"com.jiuchunjiaoyu.micro.data.wzb.common.entity"})
@EnableEurekaClient
@EnableCaching
public class Application 
{
    public static void main( String[] args ){
    	SpringApplication.run(Application.class, args);
    }
}
