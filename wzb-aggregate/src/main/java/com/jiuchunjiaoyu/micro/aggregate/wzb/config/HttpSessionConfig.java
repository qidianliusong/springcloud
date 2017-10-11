package com.jiuchunjiaoyu.micro.aggregate.wzb.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(redisNamespace = "wzb:httpsession")
public class HttpSessionConfig {

}
