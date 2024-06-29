package com.yt.app.api.v1.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.DictService;
import com.yt.app.api.v1.entity.Dict;

/**
 * @author zj defaulttest
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@RestController
@RequestMapping("/rest/v1/dict")
public class DictController extends YtBaseEncipherControllerImpl<Dict, Long> {

	
	@Autowired
	private DictService service;

	@Override
	@ApiOperation(value = "list", response = Dict.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<Dict> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@ApiOperation(value = "listfromcachebycode", response = Dict.class)
	@RequestMapping(value = "/listfromcachebycode/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> listfromcachebycode(@PathVariable String code,
			YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		List<Dict> pagebean = service.listfromcachebycode(code);
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@ApiOperation(value = "listbycode", response = Dict.class)
	@RequestMapping(value = "/listbycode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> listbycode(YtRequestDecryptEntity<Object> requestEntity,
			HttpServletRequest request, HttpServletResponse response) {
		List<Dict> pagebean = service.listbycode(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

}
