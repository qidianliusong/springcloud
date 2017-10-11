package com.jiuchunjiaoyu.micro.wzbOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 微信小程序支付启动类
 *
 */
@SpringBootApplication
@EnableEurekaClient
public class Application 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
}
