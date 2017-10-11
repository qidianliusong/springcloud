package com.jiuchunjiaoyu.micro.base.common.component.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * 内部服务器安全验证工具类
 *
 */
public class InternalServerAuthUtil {
	/**
	 * 生成token
	 * 
	 * @param clientId
	 * @param secret
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateToken(String clientId, String secret)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		long now = System.currentTimeMillis();
		String MD5 = MD5Util.getMD5(secret + now + clientId);
		StringBuilder stringBuilder = new StringBuilder(clientId);
		String content = stringBuilder.append(",").append(now).append(",").append(MD5).toString();
		byte[] encodeBase64 = Base64.encodeBase64(content.getBytes("UTF-8"));
		return new String(encodeBase64, "UTF-8");
	}

	/**
	 * 验证token
	 * @param clientId
	 * @param secret
	 * @param time
	 * @param token
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static boolean validateToken(String secret,String token,Long expireTime) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		byte[] decodeBase64 = Base64.decodeBase64(token);
		String content = new String(decodeBase64,"UTF-8");
		String[] split = content.split(",");
		if(split.length < 3)
			return false;
		String clientId = split[0];
		Long time = Long.parseLong(split[1]);
		
		if(expireTime != null && expireTime > 0){
			long now = System.currentTimeMillis();
			if(time - now > 600000){//主要应对服务器时间不一致的情形
				return false;
			}
			if(now - time > expireTime){//超出验证时间
				return false;
			}
		}
		String md5 = split[2];
		
		return md5.equals(MD5Util.getMD5(secret + time + clientId));
	}
	/**
	 * 从token中获取clientId
	 * @param token
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getClientIdFromToken(String token) throws UnsupportedEncodingException{
		byte[] decodeBase64 = Base64.decodeBase64(token);
		String content = new String(decodeBase64,"UTF-8");
		String[] split = content.split(",");
		if(split.length < 3)
			return null;
		return split[0];
	}
}
