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

	@Autowired
	private ChannelaccountorderService service;

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Channelaccountorder> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 处理充值
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/incomemanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> incomemanual(YtRequestDecryptEntity<Channelaccountorder> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		service.incomemanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

//	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<InputStreamResource> download(YtRequestDecryptEntity<Object> requestEntity,
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		ByteArrayOutputStream outputStream = service
//				.download(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
//		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
//		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=test-export.xlsx")
//				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//	}
//
//	@RequestMapping(value = "/reconciliation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<InputStreamResource> reconciliation(YtRequestDecryptEntity<Object> requestEntity,
//			HttpServletRequest request, HttpServletResponse response) throws IOException {
//		ByteArrayOutputStream outputStream = service
//				.download(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
//		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
//		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=test-export.xlsx")
//				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//	}
}
