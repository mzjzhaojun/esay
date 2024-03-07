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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.api.v1.entity.Merchantaccountorder;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@RestController
@RequestMapping("/rest/v1/merchantaccountorder")
public class MerchantaccountorderController extends YtBaseEncipherControllerImpl<Merchantaccountorder, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MerchantaccountorderService service;

	@Override
	@ApiOperation(value = "list", response = Merchantaccountorder.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Merchantaccountorder> pagebean = service
				.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/pass/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> pass(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.pass(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/turndown/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> turndown(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.turndown(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/cancle/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> cancle(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.cancle(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> withdraw(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.save(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}
	
	
	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/appwithdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> appwithdraw(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.appsave(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/passwithdraw/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> passwithdraw(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.passWithdraw(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/turndownwithdraw/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> turndownwithdraw(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.turndownWithdraw(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/canclewithdraw/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> canclewithdraw(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Object t = service.cancleWithdraw(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}
}
