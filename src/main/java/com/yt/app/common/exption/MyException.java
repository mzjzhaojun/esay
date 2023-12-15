package com.yt.app.common.exption;

import org.springframework.stereotype.Component;

import com.yt.app.common.enums.YtCodeEnum;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Component
public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private YtCodeEnum code;

	public MyException(String msg, Throwable t) {
		super(msg, t);
	}

	public MyException(String msg) {
		super(msg);
	}

	public MyException(String msg, YtCodeEnum code) {
		super(msg);
		this.code = code;
	}

	public MyException(YtCodeEnum code) {
		this.code = code;
	}

	public YtCodeEnum getCode() {
		return code;
	}

	public MyException() {
	}
}