package com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * 微信公众号feign配置
 */
public class GzhFeignConfiguration {

    @Value("${service.gzhFeign.connectTimeout:60000}")
    private int connectTimeout;

    @Value("${service.gzhFeign.readTimeOut:60000}")
    private int readTimeout;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }

}
