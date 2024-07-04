package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.AgentaccountapplyjournaService;
import com.yt.app.api.v1.entity.Agentaccountapplyjourna;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-18 12:44:01
 */

@RestController
@RequestMapping("/rest/v1/agentaccountapplyjourna")
public class AgentaccountapplyjournaController extends YtBaseEncipherControllerImpl<Agentaccountapplyjourna, Long> {

	@Autowired
	private AgentaccountapplyjournaService service;

	@Override
	@ApiOperation(value = "list", response = Agentaccountapplyjourna.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Agentaccountapplyjourna> pagebean = service
				.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
