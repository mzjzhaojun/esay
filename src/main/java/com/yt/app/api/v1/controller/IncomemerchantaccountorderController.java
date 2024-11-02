package com.yt.app.api.v1.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.IncomemerchantaccountorderService;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

@RestController
@RequestMapping("/rest/v1/incomemerchantaccountorder")
public class IncomemerchantaccountorderController extends YtBaseEncipherControllerImpl<Incomemerchantaccountorder, Long> {

	@Autowired
	private IncomemerchantaccountorderService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<IncomemerchantaccountorderVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamResource> download(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream outputStream = service.download(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=test-export.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@RequestMapping(value = "/reconciliation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamResource> reconciliation(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream outputStream = service.reconciliation(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=test-export.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
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
	public YtResponseEntity<Object> incomemanual(YtRequestDecryptEntity<Incomemerchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.incomemanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

	/**
	 * 
	 * 商户代收提现
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/incomewithdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> incomewithdraw(YtRequestDecryptEntity<Incomemerchantaccountorder> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.incomewithdraw(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 取消提现
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/incomecanclewithdraw/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> incomecanclewithdraw(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Integer t = service.incomecancleWithdraw(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 处理代收提现
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/incomewithdrawmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> incomewithdrawmanual(YtRequestDecryptEntity<Incomemerchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.incomewithdrawmanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

}
