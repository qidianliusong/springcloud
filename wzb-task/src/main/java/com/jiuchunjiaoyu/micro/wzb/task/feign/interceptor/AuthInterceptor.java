package com.jiuchunjiaoyu.micro.wzb.task.feign.interceptor;

import com.jiuchunjiaoyu.micro.base.common.component.util.InternalServerAuthUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthInterceptor implements RequestInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private String clientId;
    private String secret;

    public AuthInterceptor(String clientId, String secret) {
        this.clientId = clientId;
        this.secret = secret;
    }

    @Override
    public void apply(RequestTemplate template) {
        try {
            String sign = InternalServerAuthUtil.generateToken(clientId, secret);
            sign = "songAuth:" + sign;
            template.header("Authorization", sign);
        } catch (Exception e) {
            logger.error("生成签名信息出错", e);
        }
    }
}
