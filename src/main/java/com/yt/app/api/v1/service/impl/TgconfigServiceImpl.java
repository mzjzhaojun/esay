package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgconfigMapper;
import com.yt.app.api.v1.service.TgconfigService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgconfig;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:46:43
 */

@Service
public class TgconfigServiceImpl extends YtBaseServiceImpl<Tgconfig, Long> implements TgconfigService {
	@Autowired
	private TgconfigMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Tgconfig> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Tgconfig> list = mapper.list(param);
		return new YtPageBean<Tgconfig>(param, list, count);
	}

	@Override
	public Tgconfig get(Long id) {
		Tgconfig t = mapper.get(id);
		return t;
	}
}