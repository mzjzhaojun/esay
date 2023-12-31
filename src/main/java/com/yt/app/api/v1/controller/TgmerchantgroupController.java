package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.TgmerchantgroupService;
import com.yt.app.api.v1.entity.Tgmerchantgroup;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

@RestController
@RequestMapping("/rest/v1/tgmerchantgroup")
public class TgmerchantgroupController extends YtBaseEncipherControllerImpl<Tgmerchantgroup, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TgmerchantgroupService service;

	@Override
	@ApiOperation(value = "list", response = Tgmerchantgroup.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Tgmerchantgroup> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/putmerchant", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> putmerchant(YtRequestDecryptEntity<Tgmerchantgroup> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer t = service.putmerchant(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}
}
