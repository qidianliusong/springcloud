package com.jiuchunjiaoyu.micro.data.wzb.common.exception;

/**
 * 微智宝数据层异常
 *
 */
public class WzbDataException extends Exception {

	private static final long serialVersionUID = -1884370701153606683L;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public WzbDataException(int code, String message) {
		super(message);
		this.code = code;
	}

	public WzbDataException(int code) {
		super();
		this.code = code;
	}

	public WzbDataException(int code, String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}

	public WzbDataException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public WzbDataException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

}
