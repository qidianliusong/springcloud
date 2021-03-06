package com.jiuchunjiaoyu.micro.aggregate.wzb.feign.config;

import com.jiuchunjiaoyu.micro.aggregate.wzb.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.aggregate.wzb.feign.interceptor.AuthInterceptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

public class AuthFeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(WzbConstant.WZB_DATA_WRITE_CLIENTID, WzbConstant.WZB_DATA_WRITE_SECRET);
    }
}
