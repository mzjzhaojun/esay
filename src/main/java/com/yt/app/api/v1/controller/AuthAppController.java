package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.api.v1.bo.JwtUserBO;
import com.yt.app.api.v1.dbo.AuthLoginDTO;
import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.service.AuthService;
import com.yt.app.api.v1.service.TronmemberService;
import com.yt.app.api.v1.vo.AuthLoginVO;
import com.yt.app.common.base.constant.SecurityConstant;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.util.AuthUtil;

/**
 * @author yyds
 * 
 * @version v1
 */

@RestController
@RequestMapping("/app/v1/auth")
public class AuthAppController {

	@Autowired
	private AuthService authservice;

	@Autowired
	private TronmemberService tronmemberservice;

	@RequestMapping(value = "/loginapp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> loginapp(YtRequestDecryptEntity<AuthLoginDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		AuthLoginVO u = authservice.loginapp(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(u));
	}

	@RequestMapping(value = "/logintelegrame", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> logintelegrame(YtRequestDecryptEntity<AuthLoginDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		AuthLoginVO u = authservice.logintelegrame(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(u));
	}

	@RequestMapping(value = "/logoutapp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public YtResponseEncryptEntity<Object> logoutvue(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		AuthUtil.logout(token);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	@RequestMapping(value = "/getuser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public YtResponseEncryptEntity<Object> getUser(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(SecurityConstant.AUTHORIZATION_KEY);
		JwtUserBO jwtUserBO = AuthUtil.getLoginUser(token);
		return new YtResponseEncryptEntity<Object>(new YtBody(jwtUserBO.getUserId()));
	}

	@RequestMapping(value = "/getuseraccount/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public YtResponseEntity<Object> getuseraccount(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Tronmember tronmember = tronmemberservice.get(id);
		return new YtResponseEntity<Object>(new YtBody(tronmember));
	}

}
