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
import com.yt.app.api.v1.service.QrcodestatisticalreportsService;
import com.yt.app.api.v1.entity.Qrcodestatisticalreports;
import com.yt.app.api.v1.vo.QrcodestatisticalreportsVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2025-03-19 19:51:13
 */

@RestController
@RequestMapping("/rest/v1/qrcodestatisticalreports")
public class QrcodestatisticalreportsController extends YtBaseEncipherControllerImpl<Qrcodestatisticalreports, Long> {

	@Autowired
	private QrcodestatisticalreportsService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<QrcodestatisticalreportsVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}
}
