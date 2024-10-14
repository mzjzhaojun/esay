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
import com.yt.app.api.v1.service.TgchannelgroupService;
import com.yt.app.api.v1.entity.Tgchannelgroup;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */

@RestController
@RequestMapping("/rest/v1/tgchannelgroup")
public class TgchannelgroupController extends YtBaseEncipherControllerImpl<Tgchannelgroup, Long> {

	@Autowired
	private TgchannelgroupService service;

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Tgchannelgroup> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/putchannel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> putmerchant(YtRequestDecryptEntity<Tgchannelgroup> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer t = service.putchannel(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}
}
