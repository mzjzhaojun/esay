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
import com.yt.app.api.v1.service.QrcodetransferrecordService;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodetransferrecordVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2025-04-13 22:38:13
 */

@RestController
@RequestMapping("/rest/v1/qrcodetransferrecord")
public class QrcodetransferrecordController extends YtBaseEncipherControllerImpl<Qrcodetransferrecord, Long> {

	@Autowired
	private QrcodetransferrecordService service;

	@Autowired
	private QrcodeService qrcodeservice;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<QrcodetransferrecordVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 支付宝分账
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zfbtradeordersettle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> zfbtradeordersettle(YtRequestDecryptEntity<Qrcodetransferrecord> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		qrcodeservice.zfbtradeordersettle(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 支付宝转账
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zfbtransunitransfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> transunitransfer(YtRequestDecryptEntity<Qrcodetransferrecord> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		qrcodeservice.transunitransfer(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 易票联转账
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/epltransunitransfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> epltransunitransfer(YtRequestDecryptEntity<Qrcodetransferrecord> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		qrcodeservice.epltransunitransfer(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

}
