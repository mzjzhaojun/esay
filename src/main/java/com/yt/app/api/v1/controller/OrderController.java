package com.yt.app.api.v1.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.util.TronUtil;

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

	// 代付盘口查单
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tyquery(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request,
			HttpServletResponse response) {
		PayResultVO pt = service.query(requestEntity.getBody().getMerchantorderid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	// 代付盘口下单
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

	// 代收盘口下单
	@RequestMapping(value = "/submitqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submitqrcode(YtRequestEntity<QrcodeSubmitDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		QrcodeResultVO yb = incomeservice.submitQrcode(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	// 代收盘口查单
	@RequestMapping(value = "/queryqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> queryqrcode(YtRequestEntity<QrcodeSubmitDTO> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		QueryQrcodeResultVO yb = incomeservice.queryqrcode(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	// 宏盛代收回调
	@RequestMapping(value = "/hscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void hscallback(@RequestParam Map<String, String> params, HttpServletRequest request,
			HttpServletResponse response) {
		incomeservice.hscallback(params);
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// index
	@RequestMapping(value = "/income/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> queryIncomeOrder(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Income income = incomeservice.get(id);
		return new YtResponseEntity<Object>(new YtBody(income.getStatus()));
	}

	/**
	 * tron测试
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/trontest", method = RequestMethod.GET)
	public YtResponseEntity<Object> trontest(HttpServletRequest request, HttpServletResponse response) {
		String data = TronUtil.TestSendTron();
		return new YtResponseEntity<Object>(new YtBody(data));
	}

	/**
	 * tron测试
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/generateaddress", method = RequestMethod.GET)
	public YtResponseEntity<Object> generateaddress(HttpServletRequest request, HttpServletResponse response) {
		TronUtil.generateAddress();
		return new YtResponseEntity<Object>(new YtBody(1));
	}

}
