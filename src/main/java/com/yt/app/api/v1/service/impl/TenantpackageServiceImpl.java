package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TenantpackageMapper;
import com.yt.app.api.v1.service.TenantpackageService;

import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tenantpackage;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-01 20:08:23
 */

@Service
public class TenantpackageServiceImpl extends YtBaseServiceImpl<Tenantpackage, Long> implements TenantpackageService {
	@Autowired
	private TenantpackageMapper mapper;

	@Override
	@Transactional
	public Integer post(Tenantpackage t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override

	public YtIPage<Tenantpackage> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tenantpackage>(Collections.emptyList());
			}
		}
		List<Tenantpackage> list = mapper.list(param);
		return new YtPageBean<Tenantpackage>(param, list, count);
	}

	@Override

	public Tenantpackage get(Long id) {
		Tenantpackage t = mapper.get(id);
		return t;
	}
}