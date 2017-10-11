package com.jiuchunjiaoyu.micro.data.wzb.read.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class SystemConfig {

    private static Logger logger = LoggerFactory.getLogger(SystemConfig.class);

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

}
