package com.jiuchunjiaoyu.micro.wzbOrder.exception;

/**
 * 小程序支付异常
 *
 */
public class WzbOrderException extends Exception {

	private static final long serialVersionUID = 1L;
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public WzbOrderException(int code, String message) {
		super(message);
		this.code = code;
	}

	public WzbOrderException(int code) {
		super();
		this.code = code;
	}

	public WzbOrderException(int code, String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}

	public WzbOrderException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public WzbOrderException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

}
