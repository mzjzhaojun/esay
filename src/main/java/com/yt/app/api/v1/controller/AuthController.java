package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.yt.app.common.util.RsaUtil;

import io.swagger.annotations.ApiOperation;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@RestController
@RequestMapping("/rest/v1/auth")
public class AuthController extends YtBaseEncipherControllerImpl<User, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthService authservice;

	@ApiOperation(value = "login", response = User.class)
	@RequestMapping(value = "/loginvue", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> loginvue(YtRequestDecryptEntity<AuthLoginDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		AuthLoginVO u = authservice.login(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(u));
	}

	@ApiOperation(value = "logout", response = User.class)
	@RequestMapping(value = "/logoutvue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public YtResponseEncryptEntity<Object> logoutvue(YtRequestDecryptEntity<User> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		AuthUtil.logout(token);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	@ApiOperation(value = "getrsakey", response = User.class)
	@RequestMapping(value = "/getrsakey", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> getrsakey(HttpServletRequest request, HttpServletResponse response) {
		return new YtResponseEntity<Object>(new YtBody(RsaUtil.getPublicKey()));
	}

	@ApiOperation(value = "qrcode", response = User.class)
	@RequestMapping(value = "/qrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> qrcode(YtRequestDecryptEntity<AuthLoginDTO> requestEntity,
			HttpServletResponse response) {
		String qrcode = authservice.genQRImage(requestEntity.getBody().getUsername());
		return new YtResponseEncryptEntity<Object>(new YtBody(qrcode));
	}

	@ApiOperation(value = "verqrcode", response = User.class)
	@RequestMapping(value = "/verqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> verqrcode(YtRequestDecryptEntity<AuthLoginDTO> requestEntity,
			HttpServletResponse response) {
		Integer i = authservice.verqrcode(requestEntity.getBody().getUsername(), requestEntity.getBody().getCode(),
				requestEntity.getBody().getTwocode());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}
}
