package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.api.v1.dbo.SysQueryDTO;
import com.yt.app.api.v1.dbo.SysSubmitDTO;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

import io.swagger.annotations.ApiOperation;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@RestController
@RequestMapping("/rest/v1/order")
public class OrderController extends YtBaseEncipherControllerImpl<Payout, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PayoutService service;

	// 菲律宾代付回调
	@ApiOperation(value = "tycallback", response = User.class)
	@RequestMapping(value = "/tycallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tycallback(YtRequestEntity<SysTyOrder> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		YtBody yb = service.tycallbackpay(requestEntity.getBody());
		return new YtResponseEntity<Object>(yb);
	}

	// 盘口查单
	@ApiOperation(value = "query", response = User.class)
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tyquery(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		Payout pt = service.query(requestEntity.getBody().getMerchantorderid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	// 盘口下单
	@ApiOperation(value = "submit", response = User.class)
	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submit(YtRequestEntity<SysSubmitDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		SysResultVO sr = service.submit(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(sr));
	}

	// 盘口查询余额
	@ApiOperation(value = "query", response = User.class)
	@RequestMapping(value = "/querybalance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> query(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {

		return new YtResponseEntity<Object>(new YtBody(1));
	}

}
