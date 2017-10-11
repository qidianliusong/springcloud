package com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.config;

import com.jiuchunjiaoyu.micro.wzb.schoolBack.constant.WzbConstant;
import com.jiuchunjiaoyu.micro.wzb.schoolBack.feign.interceptor.AuthInterceptor;
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
