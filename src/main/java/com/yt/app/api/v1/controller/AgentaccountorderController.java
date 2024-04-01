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
import com.yt.app.api.v1.service.AgentaccountorderService;
import com.yt.app.api.v1.entity.Agentaccountorder;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

@RestController
@RequestMapping("/rest/v1/agentaccountorder")
public class AgentaccountorderController extends YtBaseEncipherControllerImpl<Agentaccountorder, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AgentaccountorderService service;

	@Override
	@ApiOperation(value = "list", response = Agentaccountorder.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Agentaccountorder> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> withdraw(YtRequestDecryptEntity<Agentaccountorder> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.save(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 处理提现
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/withdrawmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> withdrawmanual(YtRequestDecryptEntity<Agentaccountorder> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		service.withdrawmanual(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
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
