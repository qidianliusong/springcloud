package com.jiuchunjiaoyu.micro.aggregate.wzb.constant;
/**
 * 微智宝错误枚举
 *
 */
public enum WzbAggregateErrEnum {

	user_token_not_validate(3001,"用户token验证失败"),
	wx_pay_err(3002,"微信支付出错"),
	class_not_exists(3003,"不存在的班级"),
	user_permission_denied(3004,"用户权限不足"),
	validate_token_not_validate(3005,"validateToken验证失败"),
	wx_openid_is_null(3006,"微信openid为空"),
	amount_not_enough(3007,"余额不足"),
	company_pay_err(3008,"微信企业支付出错"),
	feedetail_status_err(3009,"收费状态错误"),
	pay_repect_err(3010,"重复支付"),
	request_params_err(3011,"请求参数有误");
	
	private int code;
	private String message;
	private WzbAggregateErrEnum(int code,String message){
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
