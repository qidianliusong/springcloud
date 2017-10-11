package com.jiuchunjiaoyu.micro.data.wzb.write.config;

import com.jiuchunjiaoyu.micro.data.wzb.write.interceptor.AuthInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * mvc配置相关
 */
@Configuration
public class WzbDataWriteWebMvcConfig extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(WzbDataWriteWebMvcConfig.class);

    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateFormatStr;

    @Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormatStr);
                Date date = null;
                try {
                    date = sdf.parse(source);
                } catch (ParseException e) {
                    logger.error("转换日期出错",e);
                }
                return date;
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger-resources/**");
        super.addInterceptors(registry);
    }
}
