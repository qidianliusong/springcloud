package com.jiuchunjiaoyu.micro.aggregate.wzb.dto.result;
/**
 * php返回值
 * @author liusong
 *
 */
public class PhpResult<T> {

	private int code;
	private String msg;
	private T data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
