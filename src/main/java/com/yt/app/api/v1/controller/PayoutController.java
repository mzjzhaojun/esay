package com.yt.app.api.v1.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.exption.YtException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
		RLock lock = RedissonUtil.getLock(pt.getUserid());
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

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<PayoutVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

}
