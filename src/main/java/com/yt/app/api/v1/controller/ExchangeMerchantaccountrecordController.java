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
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.ExchangeMerchantaccountrecordService;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountrecord;
import com.yt.app.api.v1.entity.Merchantaccountrecord;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@RestController
@RequestMapping("/rest/v1/exchangemerchantaccountrecord")
public class ExchangeMerchantaccountrecordController
		extends YtBaseEncipherControllerImpl<ExchangeMerchantaccountrecord, Long> {

	@Autowired
	private ExchangeMerchantaccountrecordService service;

	@Override
	@ApiOperation(value = "list", response = Merchantaccountrecord.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<ExchangeMerchantaccountrecord> pagebean = service
				.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
