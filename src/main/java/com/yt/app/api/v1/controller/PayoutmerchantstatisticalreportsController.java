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
import com.yt.app.api.v1.service.PayoutmerchantstatisticalreportsService;
import com.yt.app.api.v1.entity.Payoutmerchantstatisticalreports;
import com.yt.app.api.v1.vo.PayoutmerchantstatisticalreportsVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2025-06-07 23:16:10
 */

@RestController
@RequestMapping("/rest/v1/payoutmerchantstatisticalreports")
public class PayoutmerchantstatisticalreportsController extends YtBaseEncipherControllerImpl<Payoutmerchantstatisticalreports, Long> {

	@Autowired
	private PayoutmerchantstatisticalreportsService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<PayoutmerchantstatisticalreportsVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
