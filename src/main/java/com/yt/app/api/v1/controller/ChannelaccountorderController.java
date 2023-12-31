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
import com.yt.app.api.v1.service.ChannelaccountorderService;
import com.yt.app.api.v1.entity.Channelaccountorder;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

@RestController
@RequestMapping("/rest/v1/channelaccountorder")
public class ChannelaccountorderController extends YtBaseEncipherControllerImpl<Channelaccountorder, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ChannelaccountorderService service;

	@Override
	@ApiOperation(value = "list", response = Channelaccountorder.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Channelaccountorder> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
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
}
