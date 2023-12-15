package com.yt.app.common.base.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import cn.hutool.core.lang.Assert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * 
 * 实现类
 * 
 * @author zj
 * 
 */
@Service
public abstract class YtBaseServiceImpl<T, ID extends Serializable> implements YtIBaseService<T, ID> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private YtIBaseMapper<T> mapper;

	@Override
	@Transactional
	public Integer post(T t) {
		Integer i = mapper.post(t);
		Assert.equals(i, 1, ServiceConstant.ADD_FAIL_MSG);
		return i;
	}

	@Override
	@Transactional
	public Integer put(T t) {
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	public T get(Long id) {
		T t = mapper.get(id);
		Assert.notNull(t, ServiceConstant.SELECT_FAIL_MSG);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		return i;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public YtIPage<T> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		return new YtPageBean<T>(param, mapper.list(param), count);
	}

	@Override
	public List<T> list() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.list(param);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public YtIPage<List<Map<String, Object>>> map(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		return new YtPageBean<List<Map<String, Object>>>(param, count, mapper.map(param));
	}

	@Override
	public List<Map<String, Object>> map() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.map(param);
	}

	@Override
	public List<T> listByArrayId(long[] id) {
		return mapper.listByArrayId(id);
	}

}
