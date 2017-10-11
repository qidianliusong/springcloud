package com.jiuchunjiaoyu.micro.wzb.background;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 微智宝聚合服务启动类
 */
@Configuration
@SpringBootApplication(scanBasePackages = {"com.jiuchunjiaoyu.micro.wzb.background", "com.jiuchunjiaoyu.micro.base.common.redis"})
@EntityScan(basePackages={"com.jiuchunjiaoyu.micro.wzb.background.entity"})
@EnableEurekaClient
@EnableFeignClients
@EnableCaching
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}