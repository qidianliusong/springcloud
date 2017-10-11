package com.jiuchunjiaoyu.micro.wzbOrder.constant;
/**
 * 微信小程序支付报错集锦
 *
 */
public enum WzbOrderErr {

	jscode2session_err(8001,"调用微信支付:code换取session_key出错"),
	
	unifiedorder_err(8002,"调用微信支付:生成预支付交易单出错"),
	
	save_order_err(8003,"保存订单出错"),
	md5_sign_err(8004,"MD5签名出错"),
	validate_sign_err(8005,"验证签名失败"),
	wx_call_back_err(8006,"微信回调失败");
	
	private int code;
	private String message;
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
	private WzbOrderErr(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
