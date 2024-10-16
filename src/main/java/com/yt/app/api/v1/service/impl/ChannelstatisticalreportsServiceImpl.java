package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ChannelstatisticalreportsMapper;
import com.yt.app.api.v1.service.ChannelstatisticalreportsService;

import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channelstatisticalreports;
import com.yt.app.api.v1.vo.ChannelstatisticalreportsVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:03:33
 */

@Service
public class ChannelstatisticalreportsServiceImpl extends YtBaseServiceImpl<Channelstatisticalreports, Long> implements ChannelstatisticalreportsService {
	@Autowired
	private ChannelstatisticalreportsMapper mapper;

	@Override
	@Transactional
	public Integer post(Channelstatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override

	public YtIPage<Channelstatisticalreports> list(Map<String, Object> param) {
		List<Channelstatisticalreports> list = mapper.list(param);
		return new YtPageBean<Channelstatisticalreports>(list);
	}

	@Override

	public Channelstatisticalreports get(Long id) {
		Channelstatisticalreports t = mapper.get(id);
		return t;
	}

	@Override

	public YtIPage<ChannelstatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<ChannelstatisticalreportsVO>(Collections.emptyList());
		}
		List<ChannelstatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<ChannelstatisticalreportsVO>(param, list, count);
	}
}