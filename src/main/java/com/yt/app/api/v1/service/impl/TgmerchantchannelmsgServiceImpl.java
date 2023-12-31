package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.service.TgmerchantchannelmsgService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-28 15:01:59
 */

@Service
public class TgmerchantchannelmsgServiceImpl extends YtBaseServiceImpl<Tgmerchantchannelmsg, Long>
		implements TgmerchantchannelmsgService {
	@Autowired
	private TgmerchantchannelmsgMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgmerchantchannelmsg t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Tgmerchantchannelmsg> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tgmerchantchannelmsg>(Collections.emptyList());
			}
		}
		List<Tgmerchantchannelmsg> list = mapper.list(param);
		return new YtPageBean<Tgmerchantchannelmsg>(param, list, count);
	}

	@Override
	public Tgmerchantchannelmsg get(Long id) {
		Tgmerchantchannelmsg t = mapper.get(id);
		return t;
	}
}