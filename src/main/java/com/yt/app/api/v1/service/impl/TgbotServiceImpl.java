package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgbotMapper;
import com.yt.app.api.v1.service.TgbotService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgbot;
import com.yt.app.api.v1.vo.TgbotVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */

@Service
public class TgbotServiceImpl extends YtBaseServiceImpl<Tgbot, Long> implements TgbotService {

	@Autowired
	private TgbotMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgbot t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tgbot> list(Map<String, Object> param) {
		List<Tgbot> list = mapper.list(param);
		return new YtPageBean<Tgbot>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgbot get(Long id) {
		Tgbot t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TgbotVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgbotVO>(Collections.emptyList());
		}
		List<TgbotVO> list = mapper.page(param);
		list.forEach(t -> {
			t.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + t.getType()));
		});
		return new YtPageBean<TgbotVO>(param, list, count);
	}
}