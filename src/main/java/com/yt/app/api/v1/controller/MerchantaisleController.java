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
import com.yt.app.api.v1.service.MerchantaisleService;
import com.yt.app.api.v1.entity.Merchantaisle;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-11-13 10:15:12
 */

@RestController
@RequestMapping("/rest/v1/merchantaisle")
public class MerchantaisleController extends YtBaseEncipherControllerImpl<Merchantaisle, Long> {

	@Autowired
	private MerchantaisleService service;

	@Override
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Merchantaisle> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(YtRequestDecryptEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
