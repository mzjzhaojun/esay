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
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.vo.QrcodeVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

@RestController
@RequestMapping("/rest/v1/qrcode")
public class QrcodeController extends YtBaseEncipherControllerImpl<Qrcode, Long> {

	@Autowired
	private QrcodeService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<QrcodeVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/paytestzft", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> paytestzft(YtRequestDecryptEntity<Qrcode> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		QrcodeVO qv = service.paytestzft(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(qv));
	}

	@RequestMapping(value = "/paytestepl", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> paytestepl(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		String qv = service.paytestepl(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(qv));
	}

	@RequestMapping(value = "/paytesteplcafrom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> paytesteplcafrom(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		String qv = service.paytesteplcafrom(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(qv));
	}

	@RequestMapping(value = "/accountquery", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> accountquery(YtRequestDecryptEntity<Qrcode> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.accountquery(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

}
