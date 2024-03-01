package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgmerchantgroupmessageMapper;
import com.yt.app.api.v1.service.TgmerchantgroupmessageService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgmerchantgroupmessage;
import com.yt.app.api.v1.vo.TgmerchantgroupmessageVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-02-26 11:59:44
 */

@Service
public class TgmerchantgroupmessageServiceImpl extends YtBaseServiceImpl<Tgmerchantgroupmessage, Long>
		implements TgmerchantgroupmessageService {
	@Autowired
	private TgmerchantgroupmessageMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgmerchantgroupmessage t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Tgmerchantgroupmessage> list(Map<String, Object> param) {
		List<Tgmerchantgroupmessage> list = mapper.list(param);
		return new YtPageBean<Tgmerchantgroupmessage>(list);
	}

	@Override
	public Tgmerchantgroupmessage get(Long id) {
		Tgmerchantgroupmessage t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgmerchantgroupmessageVO> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<TgmerchantgroupmessageVO>(Collections.emptyList());
			}
		}
		List<TgmerchantgroupmessageVO> list = mapper.page(param);
		return new YtPageBean<TgmerchantgroupmessageVO>(param, list, count);
	}
}