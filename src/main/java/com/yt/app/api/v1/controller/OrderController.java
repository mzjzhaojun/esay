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
import com.yt.app.common.util.RequestUtil;

/**
 * @author yyds
 * 
 * @version v1
 */

@RestController
@RequestMapping("/rest/v1/order")
public class OrderController extends YtBaseEncipherControllerImpl<Payout, Long> {

	@Autowired
	private PayoutService service;

	@Autowired
	private IncomeService incomeservice;

	/**
	 * html查询代收支付状态
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/income/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> queryIncomeOrder(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Income income = incomeservice.get(id);
		return new YtResponseEntity<Object>(new YtBody(income.getStatus()));
	}

	@RequestMapping(value = "/income/income/ftyyds", method = RequestMethod.GET)
	public YtResponseEntity<Object> ftyyds(HttpServletRequest request, HttpServletResponse response) {
		Integer i = incomeservice.addip();
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	// 菲律宾代付回调
	@RequestMapping(value = "/tycallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tycallback(YtRequestEntity<SysTyOrder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtBody yb = service.tycallbackpay(requestEntity.getBody());
		return new YtResponseEntity<Object>(yb);
	}

	// 代付盘口查单
	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tyquery(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		PayResultVO pt = service.query(requestEntity.getBody().getMerchantorderid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	// 代付盘口下单
	@RequestMapping(value = "/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submit(YtRequestEntity<PaySubmitDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		PayResultVO sr = service.submit(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(sr));
	}

	// 代付盘口查询余额
	@RequestMapping(value = "/querybalance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> query(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		return new YtResponseEntity<Object>(new YtBody(100));
	}

	/**
	 * 代收本地原生支付
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitqrcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submitqrcode(YtRequestEntity<QrcodeSubmitDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		QrcodeResultVO yb = incomeservice.submitQrcode(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	/**
	 * 代收远程系统下单
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitincome", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submitincome(YtRequestEntity<QrcodeSubmitDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		QrcodeResultVO yb = incomeservice.submitInCome(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	/**
	 * 代收盘口查单
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/queryincome", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> queryincome(YtRequestEntity<QrcodeSubmitDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		QueryQrcodeResultVO yb = incomeservice.queryInCome(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	/**
	 * 宏盛代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/hscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void hscallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.hscallback(params);
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * YJJ代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/yjjcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void yjjcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.yjjcallback(params);
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 豌豆代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wdcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void wdcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.wdcallback(params);
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 日不落代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/rblcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void rblcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.rblcallback(RequestUtil.requestEntityToParamMap(requestEntity));
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 公子代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gzcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void gzcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.gzcallback(RequestUtil.requestEntityToParamMap(requestEntity));
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 玩家代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wjcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void wjcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.wjcallback(params);
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 翡翠代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fccallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void fccallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		incomeservice.fccallback(RequestUtil.requestEntityToParamMap(requestEntity));
		try {
			response.getWriter().print("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 支付宝当面付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/alipayftfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void alipayftfcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		try {
			incomeservice.alipayftfcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
