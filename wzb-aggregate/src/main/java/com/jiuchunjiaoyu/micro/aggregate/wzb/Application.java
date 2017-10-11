package com.jiuchunjiaoyu.micro.aggregate.wzb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 微智宝聚合服务启动类
 */
@Configuration
@SpringBootApplication(scanBasePackages = {"com.jiuchunjiaoyu.micro.aggregate.wzb", "com.jiuchunjiaoyu.micro.base.common.redis"})
@EnableEurekaClient
@EnableFeignClients
@EnableCaching
@EnableAsync
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}