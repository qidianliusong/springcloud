package com.jiuchunjiaoyu.micro.data.wzb.read.constant;
/**
 *写入数据报错信息
 */
public enum WzbDataReadErr {

	fee_detail_not_exists(2001,"不存在的收费详情"),
	classaccount_not_exists(2002,"不存在的班级账户"),

	fee_agent_id_not_be_null(2003,"代缴id不存在或为空"),

	id_not_be_null(2004,"id不能为空"),

    entity_not_exist(2006,"数据不存在");

	private int code;
	private String message;

	private WzbDataReadErr(int code, String message){
		this.code = code;
		this.message = message;
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
