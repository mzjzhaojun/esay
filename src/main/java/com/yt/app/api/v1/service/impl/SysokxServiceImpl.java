package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SysokxMapper;
import com.yt.app.api.v1.service.SysokxService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.api.v1.vo.SysokxVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-30 13:30:55
 */

@Service
public class SysokxServiceImpl extends YtBaseServiceImpl<Sysokx, Long> implements SysokxService {
	@Autowired
	private SysokxMapper mapper;

	@Override
	@Transactional
	public Integer post(Sysokx t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Sysokx get(Long id) {
		Sysokx t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<SysokxVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<SysokxVO>(Collections.emptyList());
		}
		List<SysokxVO> list = mapper.page(param);
		return new YtPageBean<SysokxVO>(param, list, count);
	}
}