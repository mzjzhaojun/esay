package com.yt.app.api.v1.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.api.RLock;
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
import com.yt.app.api.v1.entity.Blocklist;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.service.BlocklistService;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.RequestUtil;

import cn.hutool.json.JSONUtil;

/**
 * @author yyds
 * 
 * @version v1
 */

@RestController
@RequestMapping("/rest/v1/order")
public class OrderController {

	@Autowired
	private PayoutService payoutservice;

	@Autowired
	private IncomeService incomeservice;

	@Autowired
	private BlocklistService blocklistservice;

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

	/**
	 * 給小程序使用的訂單數據
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/incomeorderlist", method = RequestMethod.GET)
	public YtResponseEntity<Object> incomeorderlist(HttpServletRequest request, HttpServletResponse response) {
		List<Income> incomes = incomeservice.list();
		return new YtResponseEntity<Object>(new YtBody(incomes));
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
		QrcodeSubmitDTO qsdto = requestEntity.getBody();
		RLock lock = RedissonUtil.getLock(qsdto.getPay_memberid());
		QrcodeResultVO yb = null;
		try {
			lock.lock();
			yb = incomeservice.submitInCome(qsdto);
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
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
	 * kf代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/kfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void kfcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("kfcallback");
		try {
			lock.lock();
			incomeservice.kfcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * eg代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/egcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void egcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("egcallback");
		try {
			lock.lock();
			incomeservice.egcallback(params);
			response.getWriter().print("OK");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
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
		RLock lock = RedissonUtil.getLock("wdcallback");
		try {
			lock.lock();
			incomeservice.wdcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
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
		RLock lock = RedissonUtil.getLock("rblcallback");
		try {
			lock.lock();
			incomeservice.rblcallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
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
	@RequestMapping(value = "/gzcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void gzcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("gzcallback");
		try {
			lock.lock();
			incomeservice.gzcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
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
		RLock lock = RedissonUtil.getLock("wjcallback");
		try {
			lock.lock();
			incomeservice.wjcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
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
		RLock lock = RedissonUtil.getLock("fccallback");
		try {
			lock.lock();
			incomeservice.fccallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 奥克兰代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/aklcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void aklcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("aklcallback");
		try {
			lock.lock();
			incomeservice.aklcallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 新生代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void xscallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("xscallback");
		try {
			lock.lock();
			incomeservice.xscallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 飞黄运通代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fhcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void fhcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("fhcallback");
		try {
			lock.lock();
			incomeservice.fhcallback(params);
			response.getWriter().print("ok");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 易生代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/yscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void yscallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("yscallback");
		try {
			lock.lock();
			incomeservice.yscallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 张三代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void zscallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("zscallback");
		try {
			lock.lock();
			incomeservice.zscallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("ok");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 支付宝代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/alipayftfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void alipayftfcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("alipayftfcallback");
		try {
			lock.lock();
			incomeservice.alipayftfcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 通源代收回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/tongyuancallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void tongyuancallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("tongyuancallback");
		try {
			lock.lock();
			incomeservice.tongyuancallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 易票联代收通知
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/epfpayftfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> epfpayftfcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("epfpayftfcallback");
		try {
			lock.lock();
			incomeservice.epfpayftfcallback(RequestUtil.requestEntityToParamMap(requestEntity));
			String json = "{\"returnCode\":\"0000\"}";
			return new YtResponseEntity<Object>(JSONUtil.parseObj(json));
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

//	/**
//	 * 易票联代付回调
//	 * 
//	 * @param requestEntity
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/huifutxcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEntity<Object> huifutxcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
//		RLock lock = RedissonUtil.getLock("huifutxcallback");
//		try {
//			lock.lock();
//			incomeservice.huifutxcallback(RequestUtil.requestEntityToParamMap(requestEntity));
//			String json = "{\"returnCode\":\"0000\"}";
//			return new YtResponseEntity<Object>(JSONUtil.parseObj(json));
//		} catch (Exception e) {
//			throw new YtException(e);
//		} finally {
//			lock.unlock();
//		}
//	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 代付反查
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exist", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> exist(YtRequestEntity<SysTyOrder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtBody yb = payoutservice.exist(requestEntity.getBody());
		return new YtResponseEntity<Object>(yb);
	}

	/**
	 * 
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/blocklist/{md5}/{ordernum}", method = RequestMethod.GET)
	public YtResponseEntity<Object> blocklist(@PathVariable String md5, @PathVariable String ordernum, HttpServletRequest request, HttpServletResponse response) {
		Blocklist yb = blocklistservice.getByHexaddress(md5, ordernum);
		return new YtResponseEntity<Object>(new YtBody(yb));
	}

	/**
	 * 代付盘口查单
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/querypayout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tyquery(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		PayResultVO pt = payoutservice.query(requestEntity.getBody().getMerchantorderid());
		return new YtResponseEntity<Object>(new YtBody(pt));
	}

	/**
	 * 代付盘口下单
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submitpayout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> submit(YtRequestEntity<PaySubmitDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		PaySubmitDTO psdto = requestEntity.getBody();
		RLock lock = RedissonUtil.getLock(psdto.getMerchantid());
		PayResultVO sr = null;
		try {
			lock.lock();
			sr = payoutservice.submit(psdto);
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
		return new YtResponseEntity<Object>(new YtBody(sr));
	}

	/**
	 * 代付盘口查询余额
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/querybalance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> query(YtRequestEntity<SysQueryDTO> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		return new YtResponseEntity<Object>(new YtBody(100));
	}

	/**
	 * 天下代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/txcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> tycallback(YtRequestEntity<SysTyOrder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtBody yb = payoutservice.txcallbackpay(requestEntity.getBody());
		return new YtResponseEntity<Object>(yb);
	}

	/**
	 * 十年代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sncallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void sncallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("sncallback");
		try {
			lock.lock();
			payoutservice.sncallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("ok");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 盛世代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sscallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void sscallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("sscallback");
		try {
			lock.lock();
			payoutservice.sscallback(RequestUtil.requestEntityToParamMap(requestEntity));
			response.getWriter().print("ok");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 易生代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ysdfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void ysdfcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("ysdfcallback");
		try {
			lock.lock();
			payoutservice.ysdfcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 旭日代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/xrcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void xrcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("xrcallback");
		try {
			lock.lock();
			payoutservice.xrcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 守信代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sxcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void sxcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("sxcallback");
		try {
			lock.lock();
			payoutservice.sxcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 守信代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/ljcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void ljcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("ljcallback");
		try {
			lock.lock();
			payoutservice.ljcallback(params);
			response.getWriter().print("success");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * HYT代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/hytcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void hytcallback(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("hytcallback");
		try {
			lock.lock();
			payoutservice.hytcallback(params);
			response.getWriter().print("SUCCESS");
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 易票联代付回调
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/epfcallback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> epfcallback(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		RLock lock = RedissonUtil.getLock("epfcallback");
		try {
			lock.lock();
			payoutservice.epfcallback(RequestUtil.requestEntityToParamMap(requestEntity));
			String json = "{\"returnCode\":\"0000\"}";
			return new YtResponseEntity<Object>(JSONUtil.parseObj(json));
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 易票联绑卡
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sumbmitcheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> sumbmitcheck(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = RequestUtil.requestEntityToParamMap(requestEntity);
		incomeservice.sumbmitcheck(param);
		return new YtResponseEntity<Object>(new YtBody(1));
	}

	/**
	 * 易票联支付
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sumbmitcpay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> sumbmitcpay(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = RequestUtil.requestEntityToParamMap(requestEntity);
		incomeservice.sumbmitcpay(param);
		return new YtResponseEntity<Object>(new YtBody(1));
	}
}
