package com.jiuchunjiaoyu.micro.wzb.background.constant;
/**
 * 微智宝错误枚举
 *
 */
public enum WzbBackgroundErr {

	login_fail(10001,"登录失败，用户名或密码不正确"),
	session_time_out(10002,"未登录或会话已过期");


	private int code;
	private String message;
	private WzbBackgroundErr(int code, String message){
		this.setCode(code);
		this.setMessage(message);
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
