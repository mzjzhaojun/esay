package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.BanksMapper;
import com.yt.app.api.v1.service.BanksService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Banks;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-19 13:11:56
 */

@Service
public class BanksServiceImpl extends YtBaseServiceImpl<Banks, Long> implements BanksService {
	@Autowired
	private BanksMapper mapper;

	@Override
	@Transactional
	public Integer post(Banks t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Banks> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Banks> list = mapper.list(param);
		return new YtPageBean<Banks>(param, list, count);
	}

	@Override
	public Banks get(Long id) {
		Banks t = mapper.get(id);
		return t;
	}
}