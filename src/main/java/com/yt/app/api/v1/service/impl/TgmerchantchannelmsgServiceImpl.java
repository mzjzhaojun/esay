package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.service.TgmerchantchannelmsgService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.vo.TgmerchantchannelmsgVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
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
		List<Tgmerchantchannelmsg> list = mapper.list(param);
		return new YtPageBean<Tgmerchantchannelmsg>(list);
	}

	@Override
	public Tgmerchantchannelmsg get(Long id) {
		Tgmerchantchannelmsg t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgmerchantchannelmsgVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgmerchantchannelmsgVO>(Collections.emptyList());
		}
		List<TgmerchantchannelmsgVO> list = mapper.page(param);
		return new YtPageBean<TgmerchantchannelmsgVO>(param, list, count);
	}
}