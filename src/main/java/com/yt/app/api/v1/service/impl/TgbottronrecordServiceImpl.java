package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgbottronrecordMapper;
import com.yt.app.api.v1.service.TgbottronrecordService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgbottronrecord;
import com.yt.app.api.v1.vo.TgbottronrecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-05-01 14:00:37
 */

@Service
public class TgbottronrecordServiceImpl extends YtBaseServiceImpl<Tgbottronrecord, Long> implements TgbottronrecordService {
	@Autowired
	private TgbottronrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgbottronrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Tgbottronrecord get(Long id) {
		Tgbottronrecord t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgbottronrecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgbottronrecordVO>(Collections.emptyList());
		}
		List<TgbottronrecordVO> list = mapper.page(param);
		return new YtPageBean<TgbottronrecordVO>(param, list, count);
	}
}