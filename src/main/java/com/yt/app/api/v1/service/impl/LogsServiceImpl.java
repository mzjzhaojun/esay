package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.LogsMapper;
import com.yt.app.api.v1.service.LogsService;

import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Logs;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-31 13:31:43
 */

@Service
public class LogsServiceImpl extends YtBaseServiceImpl<Logs, Long> implements LogsService {
	@Autowired
	private LogsMapper mapper;

	@Override
	@Transactional
	public Integer post(Logs t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override

	public YtIPage<Logs> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Logs>(Collections.emptyList());
			}
		}
		List<Logs> list = mapper.list(param);
		return new YtPageBean<Logs>(param, list, count);
	}

	@Override

	public Logs get(Long id) {
		Logs t = mapper.get(id);
		return t;
	}
}