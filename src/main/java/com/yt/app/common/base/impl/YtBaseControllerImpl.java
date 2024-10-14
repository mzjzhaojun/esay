package com.yt.app.common.base.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.common.base.YtIBaseController;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;
import com.yt.app.common.util.RequestUtil;

import cn.hutool.core.lang.Assert;

/**
 * 
 * baseController
 * 
 * @author zj
 * 
 */
@RestController
public abstract class YtBaseControllerImpl<T, ID extends Serializable> implements YtIBaseController<T, ID> {

	@Autowired
	private YtIBaseService<T, Long> service;

	@Override
	public Long appGetCurrentUserId() {
		return SysUserContext.getUserId();
	}

	@Override
	public Long getCurrentUserReDeptId() {
		return JwtUserContext.get().getDeptId();
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> post(YtRequestEntity<T> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.post(requestEntity.getBody());
		Assert.notEquals(i, 0, "新增失败！");
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> put(YtRequestEntity<T> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.put(requestEntity.getBody());
		Assert.notEquals(i, 0, "修改失败！");
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> get(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		T t = service.get(id);
		Assert.notNull(t, "访问的资源不存在！");
		return new YtResponseEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.delete(id);
		Assert.notEquals(i, 0, "删除失败！");
		return new YtResponseEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEntity<Object> list(YtRequestEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<?> pagebean = service.list(RequestUtil.requestEntityToParamMap(requestEntity));
		return new YtResponseEntity<Object>(new YtBody(pagebean));
	}

}
