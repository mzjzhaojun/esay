package com.yt.app.api.v1.controller;

import java.util.List;

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

import cn.hutool.core.lang.Assert;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-08-23 18:22:46
 */

@RestController
@RequestMapping("/rest/v1/income")
public class IncomeController extends YtBaseEncipherControllerImpl<Income, Long> {

	@Autowired
	private IncomeService service;

	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<Income> list = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(list));
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<IncomeVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/addblock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> makeuporder(YtRequestDecryptEntity<Income> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.addblock(requestEntity.getBody());
		Assert.notEquals(i, 0, "失败！");
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	@RequestMapping(value = "/notify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> notify(YtRequestDecryptEntity<Income> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.notify(requestEntity.getBody());
		Assert.notEquals(i, 0, "更新失败！");
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	@RequestMapping(value = "/success", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> success(YtRequestDecryptEntity<Income> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.successstatus(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}
	
	@RequestMapping(value = "/settleconfirm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> settleconfirm(YtRequestDecryptEntity<Income> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		service.settleconfirm(requestEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}
}
