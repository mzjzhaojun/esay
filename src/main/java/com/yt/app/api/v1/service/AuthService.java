package com.yt.app.api.v1.service;

import javax.servlet.http.HttpServletRequest;

import com.yt.app.api.v1.dbo.AuthLoginDTO;
import com.yt.app.api.v1.vo.AuthLoginVO;

/**
 * <p>
 * 授权 服务类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 11:33
 */
public interface AuthService {

	/**
	 * 登录
	 *
	 * @param params 参数
	 * @return token
	 * @author zhengqingya
	 * @date 2020/9/21 16:18
	 */
	AuthLoginVO login(AuthLoginDTO params);

	String genQRImage(String username);

	Integer verqrcode(String username, String password, String code, String twocode);

	AuthLoginVO loginapp(AuthLoginDTO params);

	String getPublicKey(HttpServletRequest request);

}
