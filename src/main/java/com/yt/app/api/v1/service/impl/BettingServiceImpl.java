package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.BettingMapper;
import com.yt.app.api.v1.service.BettingService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.vo.BettingVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:16
 */

@Service
public class BettingServiceImpl extends YtBaseServiceImpl<Betting, Long> implements BettingService {
	@Autowired
	private BettingMapper mapper;

	@Override
	@Transactional
	public Integer post(Betting t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Betting get(Long id) {
		Betting t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<BettingVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<BettingVO>(Collections.emptyList());
		}
		List<BettingVO> list = mapper.page(param);
		return new YtPageBean<BettingVO>(param, list, count);
	}
}