package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.RolescopeService;
import com.yt.app.api.v1.entity.Rolescope;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

@RestController
@RequestMapping("/rest/v1/rolescope")
public class RolescopeController extends YtBaseEncipherControllerImpl<Rolescope, Long> {

	
	@Autowired
	private RolescopeService service;

	@Override
	@ApiOperation(value = "list", response = Rolescope.class)
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Rolescope> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
