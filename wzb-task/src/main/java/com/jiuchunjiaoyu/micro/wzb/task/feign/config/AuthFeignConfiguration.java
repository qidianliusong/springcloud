package com.jiuchunjiaoyu.micro.wzb.task.feign.config;

import com.jiuchunjiaoyu.micro.wzb.task.constant.Constant;
import com.jiuchunjiaoyu.micro.wzb.task.feign.interceptor.AuthInterceptor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

public class AuthFeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(Constant.WZB_DATA_WRITE_CLIENTID, Constant.WZB_DATA_WRITE_SECRET);
    }
}
