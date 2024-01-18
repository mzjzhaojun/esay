package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SysbankMapper;
import com.yt.app.api.v1.service.SysbankService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Sysbank;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 11:28:36
 */

@Service
public class SysbankServiceImpl extends YtBaseServiceImpl<Sysbank, Long> implements SysbankService {
	@Autowired
	private SysbankMapper mapper;

	@Override
	@Transactional
	public Integer post(Sysbank t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Sysbank> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Sysbank>(Collections.emptyList());
			}
		}
		List<Sysbank> list = mapper.list(param);
		return new YtPageBean<Sysbank>(param, list, count);
	}

	@Override
	public Sysbank get(Long id) {
		Sysbank t = mapper.get(id);
		return t;
	}
}