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
public class YtException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private YtCodeEnum code;

	public YtException(String msg, Throwable t) {
		super(msg, t);
	}

	public YtException(String msg) {
		super(msg);
		this.code = YtCodeEnum.YT888;
	}

	public YtException(String msg, YtCodeEnum code) {
		super(msg);
		this.code = code;
	}

	public YtException(YtCodeEnum code) {
		this.code = code;
	}

	public YtCodeEnum getCode() {
		return code;
	}

	public YtException() {
	}
}