package com.yt.app.common.base.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.lang.Assert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
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

	@Autowired
	private YtIBaseMapper<T> mapper;

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer post(T t) {
		Integer i = mapper.post(t);
		Assert.equals(i, 1, ServiceConstant.ADD_FAIL_MSG);
		return i;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer put(T t) {
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public T get(Long id) {
		T t = mapper.get(id);
		Assert.notNull(t, ServiceConstant.SELECT_FAIL_MSG);
		return t;
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer delete(Long id) {
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<T> list() {
		Map<String, Object> param = new HashMap<String, Object>();
		return mapper.list(param);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<T> list(Map<String, Object> param) {
		return mapper.list(param);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<T> listByArrayId(long[] id) {
		return mapper.listByArrayId(id);
	}

}
