package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.entity.Agentaccount;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@RestController
@RequestMapping("/rest/v1/agentaccount")
public class AgentaccountController extends YtBaseEncipherControllerImpl<Agentaccount, Long> {

	
	@Autowired
	private AgentaccountService service;

	@Override
	@ApiOperation(value = "list", response = Agentaccount.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Agentaccount> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> data(HttpServletRequest request, HttpServletResponse response) {
		Agentaccount t = service.getData();
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/bank", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> bank(HttpServletRequest request, HttpServletResponse response) {
		Agentaccount t = service.getDataBank();
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/bank/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> bank(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Agentaccount t = service.getDataBank(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}
}
