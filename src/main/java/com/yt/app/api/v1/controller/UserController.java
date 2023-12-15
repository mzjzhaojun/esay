package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;
import io.swagger.annotations.ApiOperation;

import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.UserService;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.User;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */

@RestController
@RequestMapping("/rest/v1/user")
public class UserController extends YtBaseEncipherControllerImpl<User, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserService service;

	@Override
	@ApiOperation(value = "list", response = User.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<User> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/getUserPerm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getUserPerm(YtRequestDecryptEntity<SysUserPermDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		SysUserPermVO suserperm = service
				.getUserPerm(SysUserPermDTO.builder().userId(SysUserContext.getUserId()).build());
		return new YtResponseEncryptEntity<Object>(new YtBody(suserperm));
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> resetpassword(YtRequestDecryptEntity<User> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.resetpassword(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

}
