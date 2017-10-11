package com.jiuchunjiaoyu.micro.data.wzb.write.config;

import java.util.HashSet;

/**
 * 安全验证配置
 *
 */
public class AuthConfig {

	/**
	 * 本服务的密钥
	 */
	public static String secret = "QsnFjwJrZmbRYsjHhrhL";
	
	public static long tokenExpireTime = 300000;//5分钟失效
	
	private static HashSet<String> clientIds = new HashSet<>();
	static{
		clientIds.add("7dde57b203714c269883a3a3205c0b26");
		clientIds.add("cc1d06b584b94a218611cdb7e88becfc");
		clientIds.add("c557b6ca323d4be0820700007ac58476");
		clientIds.add("565bc94dd52546c1b55ef7fd80afbf75");
		clientIds.add("3efb60f5584f454eb0f74912ffcd9ce2");
		clientIds.add("73410fe687154e49a0a8928e4ddce11b");
		clientIds.add("71c8eb363ff74ad9b568c4b6e03d9a71");
	}
	
	public static boolean validateClient(String clientId){
		return clientIds.contains(clientId);
	}

}
