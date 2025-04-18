package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgbotgroupMapper;
import com.yt.app.api.v1.service.TgbotgroupService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgbotgroup;
import com.yt.app.api.v1.vo.TgbotgroupVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */

@Service
public class TgbotgroupServiceImpl extends YtBaseServiceImpl<Tgbotgroup, Long> implements TgbotgroupService {
	@Autowired
	private TgbotgroupMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgbotgroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgbotgroup get(Long id) {
		Tgbotgroup t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TgbotgroupVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgbotgroupVO>(Collections.emptyList());
		}
		List<TgbotgroupVO> list = mapper.page(param);
		return new YtPageBean<TgbotgroupVO>(param, list, count);
	}
}