package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.entity.Sysokx;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

@RestController
@RequestMapping("/rest/v1/sysconfig")
public class SysconfigController extends YtBaseEncipherControllerImpl<Sysconfig, Long> {

	@Autowired
	private SysconfigService service;

	@Override
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Sysconfig> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/exchange", method = RequestMethod.GET)
	public YtResponseEncryptEntity<Object> exchange(HttpServletRequest request, HttpServletResponse response) {
		Sysokx sysconfig = service.getDataTopOne();
		return new YtResponseEncryptEntity<Object>(new YtBody(sysconfig));
	}
}
