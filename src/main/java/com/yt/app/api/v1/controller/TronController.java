package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2024-09-06 16:03:13
 */

@RestController
@RequestMapping("/rest/v1/tron")
public class TronController extends YtBaseEncipherControllerImpl<Tron, Long> {

	@Autowired
	private TronService service;

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Tron> list = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(list));
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<TronVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
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
		return new YtResponseEntity<Object>(new YtBody(1));
	}

	@RequestMapping(value = "/getnodeinfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getnodeinfo(HttpServletRequest request, HttpServletResponse response) {
		service.getnodeinfo();
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}
}
