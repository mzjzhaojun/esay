package com.yt.app.common.common;

import java.io.Serializable;

public class Ytf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 接口访问凭证
	private String accessToken;
	// 凭证有效期，单位：秒
	private int expiresIn;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Override
	public String toString() {
		return "AssessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + "]";
	}

}
