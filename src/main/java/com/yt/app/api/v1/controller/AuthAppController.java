package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.api.v1.dbo.AuthLoginDTO;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.service.AuthService;
import com.yt.app.api.v1.vo.AuthLoginVO;
import com.yt.app.common.base.constant.SecurityConstant;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.util.AuthUtil;

import io.swagger.annotations.ApiOperation;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@RestController
@RequestMapping("/app/v1/auth")
public class AuthAppController extends YtBaseEncipherControllerImpl<User, Long> {

	@Autowired
	private AuthService authservice;

	@ApiOperation(value = "loginapp", response = User.class)
	@RequestMapping(value = "/loginapp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> loginapp(YtRequestDecryptEntity<AuthLoginDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		AuthLoginVO u = authservice.loginapp(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(u));
	}

	@ApiOperation(value = "logoutapp", response = User.class)
	@RequestMapping(value = "/logoutapp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public YtResponseEncryptEntity<Object> logoutvue(YtRequestDecryptEntity<User> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		AuthUtil.logout(token);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

}
