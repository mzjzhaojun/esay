package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ConfigMapper;
import com.yt.app.api.v1.service.ConfigService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Config;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-02 20:38:10
 */

@Service
public class ConfigServiceImpl extends YtBaseServiceImpl<Config, Long> implements ConfigService {
	@Autowired
	private ConfigMapper mapper;

	@Override
	@Transactional
	public Integer post(Config t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Config> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Config>(Collections.emptyList());
			}
		}
		List<Config> list = mapper.list(param);
		return new YtPageBean<Config>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Config get(Long id) {
		Config t = mapper.get(id);
		return t;
	}
}