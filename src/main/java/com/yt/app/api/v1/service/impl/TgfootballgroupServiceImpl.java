package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgfootballgroupMapper;
import com.yt.app.api.v1.service.TgfootballgroupService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgfootballgroup;
import com.yt.app.api.v1.vo.TgfootballgroupVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-15 17:34:22
 */

@Service
public class TgfootballgroupServiceImpl extends YtBaseServiceImpl<Tgfootballgroup, Long> implements TgfootballgroupService {
	@Autowired
	private TgfootballgroupMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgfootballgroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Tgfootballgroup get(Long id) {
		Tgfootballgroup t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgfootballgroupVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgfootballgroupVO>(Collections.emptyList());
		}
		List<TgfootballgroupVO> list = mapper.page(param);
		return new YtPageBean<TgfootballgroupVO>(param, list, count);
	}
}