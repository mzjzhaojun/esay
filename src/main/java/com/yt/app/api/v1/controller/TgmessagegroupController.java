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

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.TgmessagegroupService;
import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.vo.TgmessagegroupVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

@RestController
@RequestMapping("/rest/v1/tgmessagegroup")
public class TgmessagegroupController extends YtBaseEncipherControllerImpl<Tgmessagegroup, Long> {

	@Autowired
	private TgmessagegroupService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<TgmessagegroupVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/putmerchant", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> putmerchant(YtRequestDecryptEntity<Tgmessagegroup> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer t = service.putmerchant(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

}
