package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.TgmerchantgroupService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.vo.TgmerchantgroupVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

@Service
public class TgmerchantgroupServiceImpl extends YtBaseServiceImpl<Tgmerchantgroup, Long>
		implements TgmerchantgroupService {
	@Autowired
	private TgmerchantgroupMapper mapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private ChannelMapper channelmanager;

	@Override
	@Transactional
	public Integer post(Tgmerchantgroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Tgmerchantgroup> list(Map<String, Object> param) {
		List<Tgmerchantgroup> list = mapper.list(param);
		return new YtPageBean<Tgmerchantgroup>(list);
	}

	@Override
	public Tgmerchantgroup get(Long id) {
		Tgmerchantgroup t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgmerchantgroupVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgmerchantgroupVO>(Collections.emptyList());
		}
		List<TgmerchantgroupVO> list = mapper.page(param);
		return new YtPageBean<TgmerchantgroupVO>(param, list, count);
	}

	@Override
	@Transactional
	public Integer putmerchant(Tgmerchantgroup t) {
		Merchant mt = merchantmapper.get(t.getMerchantid());
		Channel cl = channelmanager.get(t.getChannelid());
		t.setChannelname(cl.getName());
		t.setMerchantname(mt.getName());
		return mapper.put(t);
	}
}