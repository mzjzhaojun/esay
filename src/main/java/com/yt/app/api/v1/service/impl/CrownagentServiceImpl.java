package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.CrownagentMapper;
import com.yt.app.api.v1.service.CrownagentService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.vo.CrownagentVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */

@Service
public class CrownagentServiceImpl extends YtBaseServiceImpl<Crownagent, Long> implements CrownagentService {
	@Autowired
	private CrownagentMapper mapper;

	@Override
	@Transactional
	public Integer post(Crownagent t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Crownagent get(Long id) {
		Crownagent t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<CrownagentVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<CrownagentVO>(Collections.emptyList());
		}
		List<CrownagentVO> list = mapper.page(param);
		return new YtPageBean<CrownagentVO>(param, list, count);
	}

	@Override
	public Integer login(Crownagent t) {
		
		
		
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}
}