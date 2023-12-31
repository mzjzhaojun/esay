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
import com.yt.app.api.v1.service.TgmerchantgroupmessageService;
import com.yt.app.api.v1.entity.Tgmerchantgroupmessage;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

@RestController
@RequestMapping("/rest/v1/tgmerchantgroupmessage")
public class TgmerchantgroupmessageController extends YtBaseEncipherControllerImpl<Tgmerchantgroupmessage, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TgmerchantgroupmessageService service;

	@Override
	@ApiOperation(value = "list", response = Tgmerchantgroupmessage.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Tgmerchantgroupmessage> pagebean = service
				.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
