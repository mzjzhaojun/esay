package com.yt.app.api.v1.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.exption.YtException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.RequestUtil;

import cn.hutool.core.lang.Assert;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.entity.Payout;

/**
 * @author yyds
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

@RestController
@RequestMapping("/rest/v1/payout")
public class PayoutController extends YtBaseEncipherControllerImpl<Payout, Long> {

	@Autowired
	private PayoutService service;

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> post(YtRequestDecryptEntity<Payout> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		Payout pt = YtRequestDecryptEntity.getBody();
		RLock lock = RedissonUtil.getLock(pt.getMerchantid());
		Integer i = 0;
		try {
			lock.lock();
			i = service.submitOrder(pt);
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
		Assert.notEquals(i, 0, "新增失败！");
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@RequestMapping(value = "/selfadd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> selfadd(YtRequestDecryptEntity<Payout> YtRequestDecryptEntity, HttpServletRequest request, HttpServletResponse response) {
		Payout pt = YtRequestDecryptEntity.getBody();
		RLock lock = RedissonUtil.getLock(pt.getMerchantid());
		Integer i = 0;
		try {
			lock.lock();
			i = service.submitOrderSelf(pt);
		} catch (Exception e) {
			throw new YtException(e);
		} finally {
			lock.unlock();
		}
		Assert.notEquals(i, 0, "新增失败！");
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 批量上传代付渠道
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> upload(MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");
		String aisleid = request.getParameter("aisleid");
		Assert.notNull(aisleid, "请选择通道后再上传");
		String name = "";
		try {
			name = service.upFile(file, aisleid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new YtResponseEntity<Object>(new YtBody(name));
	}

	/**
	 * 批量上传代付渠道
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/self/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> uploadself(MultipartHttpServletRequest request) {
		MultipartFile file = request.getFile("file");
		String aisleid = request.getParameter("aisleid");
		Assert.notNull(aisleid, "请选择通道后再上传");
		String name = "";
		try {
			name = service.uploadself(file, aisleid);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new YtResponseEntity<Object>(new YtBody(name));
	}

	/**
	 * 下载
	 * 
	 * @param requestEntity
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InputStreamResource> download(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ByteArrayOutputStream outputStream = service.download(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=test-export.xlsx").contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	/**
	 * 分页
	 */
	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<PayoutVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	/**
	 * 成功
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/success/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> success(@PathVariable Long id, HttpServletRequest request) {
		Integer i = service.success(id);
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 失败
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fail/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> fail(@PathVariable Long id, HttpServletRequest request) {
		Integer i = service.fail(id);
		return new YtResponseEntity<Object>(new YtBody(i));
	}
	
	
	/**
	 * 申請回單
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/handleCertificate/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> handleCertificate(@PathVariable Long id, HttpServletRequest request) {
		Integer i = service.handleCertificate(id);
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 下载回單
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/handleCertificateDownload/{id}", method = RequestMethod.GET)
	public YtResponseEntity<Object> handleCertificateDownload(@PathVariable Long id, HttpServletRequest request) {
		Integer i = service.handleCertificateDownload(id);
		return new YtResponseEntity<Object>(new YtBody(i));
	}

}
