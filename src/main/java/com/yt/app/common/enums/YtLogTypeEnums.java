package com.yt.app.common.enums;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public enum YtLogTypeEnums {

	LOG_TYPE_EXCEPTION(0), LOG_TYPE_OPERATION(1);

	private Integer logType;

	YtLogTypeEnums(Integer logType) {
		this.logType = logType;
	}

	public Integer getValue() {
		return logType;
	}
}
