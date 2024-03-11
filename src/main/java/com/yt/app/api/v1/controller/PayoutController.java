package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

@RestController
@RequestMapping("/rest/v1/payout")
public class PayoutController extends YtBaseEncipherControllerImpl<Payout, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private PayoutService service;

	// 手动回调成功
	@ApiOperation(value = "payoutmanual", response = User.class)
	@RequestMapping(value = "/payoutmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> payoutmanual(YtRequestDecryptEntity<Payout> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		service.payoutmanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

	@Override
	@ApiOperation(value = "list", response = Payout.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Payout> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@ApiOperation(value = "page", response = Payout.class)
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<PayoutVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

}
