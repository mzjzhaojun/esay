package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgmessagegrouprecordMapper;
import com.yt.app.api.v1.service.TgmessagegrouprecordService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgmessagegrouprecord;
import com.yt.app.api.v1.vo.TgmessagegrouprecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-09-19 01:40:22
 */

@Service
public class TgmessagegrouprecordServiceImpl extends YtBaseServiceImpl<Tgmessagegrouprecord, Long> implements TgmessagegrouprecordService {
	@Autowired
	private TgmessagegrouprecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgmessagegrouprecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Tgmessagegrouprecord get(Long id) {
		Tgmessagegrouprecord t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgmessagegrouprecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgmessagegrouprecordVO>(Collections.emptyList());
		}
		List<TgmessagegrouprecordVO> list = mapper.page(param);
		return new YtPageBean<TgmessagegrouprecordVO>(param, list, count);
	}
}