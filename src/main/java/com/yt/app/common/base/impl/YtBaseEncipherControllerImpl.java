package com.yt.app.common.base.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yt.app.common.base.YtIBaseEncipherController;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;
import com.yt.app.common.util.RequestUtil;

/**
 * 
 * baseController
 * 
 * @author zj
 * 
 */
@RestController
public abstract class YtBaseEncipherControllerImpl<T, ID extends Serializable>
		implements YtIBaseEncipherController<T, ID> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private YtIBaseService<T, Long> service;

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> post(YtRequestDecryptEntity<T> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.post(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> put(YtRequestDecryptEntity<T> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		Integer i = service.put(YtRequestDecryptEntity.getBody());
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> get(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		T t = service.get(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(t));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> delete(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response) {
		Integer i = service.delete(id);
		return new YtResponseEncryptEntity<Object>(new YtBody(i));
	}

	/**
	 * 
	 * 
	 * @version 1.1
	 */
	@Override
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> YtRequestDecryptEntity,
			HttpServletRequest request, HttpServletResponse response) {
		YtIPage<?> pagebean = service.list(RequestUtil.requestDecryptEntityToParamMap(YtRequestDecryptEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

}
