package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TwitterMapper;
import com.yt.app.api.v1.service.TwitterService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Twitter;
import com.yt.app.api.v1.vo.TwitterVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-20 17:29:27
 */

@Service
public class TwitterServiceImpl extends YtBaseServiceImpl<Twitter, Long> implements TwitterService {
	@Autowired
	private TwitterMapper mapper;

	@Override
	@Transactional
	public Integer post(Twitter t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Twitter> list(Map<String, Object> param) {
		List<Twitter> list = mapper.list(param);
		return new YtPageBean<Twitter>(list);
	}

	@Override
	public Twitter get(Long id) {
		Twitter t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TwitterVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TwitterVO>(Collections.emptyList());
		}
		List<TwitterVO> list = mapper.page(param);
		return new YtPageBean<TwitterVO>(param, list, count);
	}
}