package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.service.TgchannelgroupService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */

@Service
public class TgchannelgroupServiceImpl extends YtBaseServiceImpl<Tgchannelgroup, Long> implements TgchannelgroupService {
	@Autowired
	private TgchannelgroupMapper mapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Override
	@Transactional
	public Integer post(Tgchannelgroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tgchannelgroup> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tgchannelgroup>(Collections.emptyList());
			}
		}
		List<Tgchannelgroup> list = mapper.list(param);
		return new YtPageBean<Tgchannelgroup>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgchannelgroup get(Long id) {
		Tgchannelgroup t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer putchannel(Tgchannelgroup t) {
		Channel ct = channelmapper.get(t.getChannelid());
		t.setChannelname(ct.getName());
		t.setChannelid(t.getChannelid());
		t.setTenant_id(ct.getTenant_id());
		return mapper.put(t);
	}
}