package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.entity.Payout;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

@RestController
@RequestMapping("/rest/v1/payout")
public class PayoutController extends YtBaseEncipherControllerImpl<Payout, Long> {

	@Autowired
	private PayoutService service;

	// 手动回调成功
	@RequestMapping(value = "/payoutmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> payoutmanual(YtRequestDecryptEntity<Payout> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.payoutmanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<PayoutVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

}
