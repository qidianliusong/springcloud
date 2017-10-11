package com.jiuchunjiaoyu.micro.wzb.background.feign.config;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * classFeign配置
 */
public class ClassFeignConfiguration {

    @Value("${service.classFeign.connectTimeout:60000}")
    private int connectTimeout;

    @Value("${service.classFeign.readTimeOut:60000}")
    private int readTimeout;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }

}
