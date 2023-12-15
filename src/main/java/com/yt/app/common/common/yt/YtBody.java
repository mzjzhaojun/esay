package com.yt.app.common.common.yt;

import com.yt.app.common.enums.YtCodeEnum;

public class YtBody {

	private Integer code;

	private Object body;

	private String msg;

	public Integer getCode() {
		return this.code;
	}

	public Object getBody() {
		return this.body;
	}

	public Object setBody(Object body) {
		return this.body = body;
	}

	public String getMsg() {
		return msg;
	}

	public YtBody(Object body, Integer code, String msg) {
		this.code = code;
		this.body = body;
		this.msg = msg;
	}

	public YtBody(String msg, Integer code) {
		this.code = code;
		this.body = msg;
		this.msg = msg;
	}

	public YtBody(Object body, Integer code) {
		this.code = code;
		this.body = body;
		this.msg = YtCodeEnum.getEnum(code).getDesc();
	}

	public YtBody(Object body) {
		this.msg = YtCodeEnum.YT200.getDesc();
		this.code = YtCodeEnum.YT200.getCode();
		this.body = body;
	}

	public YtBody() {
		this.code = YtCodeEnum.YT200.getCode();
		this.body = YtCodeEnum.getEnum(code).getDesc();
		this.msg = YtCodeEnum.getEnum(code).getDesc();
	}
}
