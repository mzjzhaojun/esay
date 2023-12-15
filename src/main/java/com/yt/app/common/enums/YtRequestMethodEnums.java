package com.yt.app.common.enums;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public enum YtRequestMethodEnums {

	GET("GET"), LIST("LIST"), POST("POST"), PUT("PUT"), DELETE("DELETE"), NULL("NULL");

	private String methodtype;

	YtRequestMethodEnums(String methodtype) {
		this.methodtype = methodtype;
	}

	public String getValue() {
		return methodtype;
	}
}
