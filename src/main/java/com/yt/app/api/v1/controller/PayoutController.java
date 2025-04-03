package com.yt.app.api.v1.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	 * 普通上传
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> add(MultipartHttpServletRequest request) {
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

}
