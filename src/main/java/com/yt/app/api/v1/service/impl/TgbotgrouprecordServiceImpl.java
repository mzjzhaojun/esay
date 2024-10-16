package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgbotgrouprecordMapper;
import com.yt.app.api.v1.service.TgbotgrouprecordService;

import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgbotgrouprecord;
import com.yt.app.api.v1.vo.TgbotgrouprecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */

@Service
public class TgbotgrouprecordServiceImpl extends YtBaseServiceImpl<Tgbotgrouprecord, Long> implements TgbotgrouprecordService {
	@Autowired
	private TgbotgrouprecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgbotgrouprecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override

	public YtIPage<Tgbotgrouprecord> list(Map<String, Object> param) {
		List<Tgbotgrouprecord> list = mapper.list(param);
		return new YtPageBean<Tgbotgrouprecord>(list);
	}

	@Override

	public Tgbotgrouprecord get(Long id) {
		Tgbotgrouprecord t = mapper.get(id);
		return t;
	}

	@Override

	public YtIPage<TgbotgrouprecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgbotgrouprecordVO>(Collections.emptyList());
		}
		List<TgbotgrouprecordVO> list = mapper.page(param);
		return new YtPageBean<TgbotgrouprecordVO>(param, list, count);
	}
}