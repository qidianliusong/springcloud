package com.jiuchunjiaoyu.micro.wzbGateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 微智宝网关项目启动类
 *
 */
@EnableZuulProxy
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}
}
