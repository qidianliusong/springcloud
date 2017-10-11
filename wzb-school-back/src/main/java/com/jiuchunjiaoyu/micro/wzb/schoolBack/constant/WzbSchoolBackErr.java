package com.jiuchunjiaoyu.micro.wzb.schoolBack.constant;
/**
 * 微智宝错误枚举
 *
 */
public enum WzbSchoolBackErr {

	user_token_not_validate(7001,"用户token验证失败"),
	class_not_exists(7003,"不存在的班级"),
	user_permission_denied(7004,"用户权限不足"),
	validate_token_not_validate(7005,"validateToken验证失败"),
	request_params_err(7011,"请求参数有误");
	
	private int code;
	private String message;
	private WzbSchoolBackErr(int code, String message){
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
