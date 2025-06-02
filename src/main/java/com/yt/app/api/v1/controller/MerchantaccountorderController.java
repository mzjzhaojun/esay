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
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.api.v1.vo.MerchantaccountorderVO;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

@RestController
@RequestMapping("/rest/v1/merchantaccountorder")
public class MerchantaccountorderController extends YtBaseEncipherControllerImpl<Merchantaccountorder, Long> {

	@Autowired
	private MerchantaccountorderService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<MerchantaccountorderVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamResource> download(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream outputStream = service.download(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
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
	public YtResponseEntity<Object> incomemanual(YtRequestDecryptEntity<Merchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
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
	public YtResponseEncryptEntity<Object> incomewithdraw(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.incomewithdraw(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 商户代收提现
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/incomewithdrawapp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> incomewithdrawapp(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		Long id = service.incomewithdrawapp(YtRequestDecryptEntity.getBody());
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Integer i = service.success(id);
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
	public YtResponseEntity<Object> incomewithdrawmanual(YtRequestDecryptEntity<Merchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.incomewithdrawmanual(requestEntity.getBody());
		return new YtResponseEntity<Object>(new YtBody(1));
	}

//	/**
//	 * app提现
//	 * 
//	 * @version 1.1
//	 */
//	@RequestMapping(value = "/appwithdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEncryptEntity<Object> appwithdraw(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
//		Integer i = service.appsave(YtRequestDecryptEntity.getBody());
//		return new YtResponseEncryptEntity<Object>(new YtBody(i));
//	}
//
//	/**
//	 * 
//	 * 取消充值
//	 * 
//	 * @version 1.1
//	 */
//	@RequestMapping(value = "/cancle/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEncryptEntity<Object> cancle(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
//		Object t = service.cancle(id);
//		return new YtResponseEncryptEntity<Object>(new YtBody(t));
//	}
//
//	/**
//	 * 
//	 * 提现
//	 * 
//	 * @version 1.1
//	 */
//	@RequestMapping(value = "/withdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEncryptEntity<Object> withdraw(YtRequestDecryptEntity<Merchantaccountorder> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
//		Integer i = service.save(YtRequestDecryptEntity.getBody());
//		return new YtResponseEncryptEntity<Object>(new YtBody(i));
//	}
//
//	/**
//	 * 取消提现
//	 * 
//	 * @version 1.1
//	 */
//	@RequestMapping(value = "/canclewithdraw/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEncryptEntity<Object> canclewithdraw(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
//		Integer t = service.cancleWithdraw(id);
//		return new YtResponseEncryptEntity<Object>(new YtBody(t));
//	}
//
//	/**
//	 * 处理提现
//	 * 
//	 * @param requestEntity
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/withdrawmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEntity<Object> withdrawmanual(YtRequestDecryptEntity<Merchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
//		service.withdrawmanual(requestEntity.getBody());
//		return new YtResponseEntity<Object>(new YtBody(1));
//	}
//
//	/**
//	 * 处理充值
//	 * 
//	 * @param requestEntity
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value = "/payoutmanual", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public YtResponseEntity<Object> payoutincomemanual(YtRequestDecryptEntity<Merchantaccountorder> requestEntity, HttpServletRequest request, HttpServletResponse response) {
//		service.incomemanual(requestEntity.getBody());
//		return new YtResponseEntity<Object>(new YtBody(1));
//	}

}
