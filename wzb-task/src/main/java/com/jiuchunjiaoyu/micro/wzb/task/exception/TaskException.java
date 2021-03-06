package com.jiuchunjiaoyu.micro.wzb.task.exception;

/**
 * 微智宝聚合层异常
 *
 */
public class TaskException extends Exception {

	private static final long serialVersionUID = -1884370701153606683L;

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public TaskException(int code, String message) {
		super(message);
		this.code = code;
	}

	public TaskException(int code) {
		super();
		this.code = code;
	}

	public TaskException(int code, String message, Throwable cause, boolean enableSuppression,
						 boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}

	public TaskException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public TaskException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

}
