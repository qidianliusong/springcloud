package com.jiuchunjiaoyu.micro.wzb.background.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(redisNamespace = "wzb-background:httpsession")
public class HttpSessionConfig {

}
