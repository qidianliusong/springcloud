package com.jiuchunjiaoyu.micro.zippkinServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import zipkin.server.EnableZipkinServer;

/**
 * 链路追踪服务器启动类
 *
 */
@SpringBootApplication
@EnableZipkinServer
public class Application 
{
    public static void main( String[] args )
    {
    	 SpringApplication.run(Application.class, args);
    }
}
