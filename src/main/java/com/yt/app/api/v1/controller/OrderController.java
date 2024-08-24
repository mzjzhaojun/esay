package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.api.v1.dbo.SysQueryDTO;
import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2018-09-27 09:52:46
 */

@RestController
@RequestMapping("/rest/v1/order")
public class OrderController extends YtBaseEncipherControllerImpl<Payout, Long> {

	@Autowired
	private PayoutService service;

	@Autowired
	private IncomeService incomeservice;

	// 菲律宾代付回调
	@RequestMapping(value = "/tycallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tycallback(YtRequestEntity<SysTyOrder> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		YtBody yb = service.tycallbackpay(requestEntity.getBody());
		return new YtResponseEntity<Object>(yb);
	}

	// 盘口查单
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tyquery(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		PayResultVO pt = service.query(requestEntity.getBody().getMerchantorderid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	// 盘口下单
	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submit(YtRequestEntity<PaySubmitDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		PayResultVO sr = service.submit(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(sr));
	}

	// 盘口查询余额
	@RequestMapping(value = "/querybalance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> query(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		PayResultVO pt = service.query(requestEntity.getBody().getMerchantid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	// 拉码下单
	@RequestMapping(value = "/submitqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submitqrcode(YtRequestEntity<QrcodeSubmitDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		QrcodeResultVO yb = incomeservice.submitQrcode(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	// 拉码查单
	@RequestMapping(value = "/queryqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> queryqrcode(YtRequestEntity<QrcodeSubmitDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		QrcodeResultVO yb = incomeservice.queryqrcode(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	// index
	@RequestMapping(value = "/income/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> queryIncomeOrder(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Income income = incomeservice.get(id);
		return new YtResponseEntity<Object>(new YtBody(income.getStatus()));
	}

}
