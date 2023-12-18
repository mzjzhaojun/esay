package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgmerchantgrouplabelMapper;
import com.yt.app.api.v1.service.TgmerchantgrouplabelService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgmerchantgrouplabel;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

@Service
public class TgmerchantgrouplabelServiceImpl extends YtBaseServiceImpl<Tgmerchantgrouplabel, Long>
		implements TgmerchantgrouplabelService {
	@Autowired
	private TgmerchantgrouplabelMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgmerchantgrouplabel t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tgmerchantgrouplabel> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tgmerchantgrouplabel>(Collections.emptyList());
			}
		}
		List<Tgmerchantgrouplabel> list = mapper.list(param);
		return new YtPageBean<Tgmerchantgrouplabel>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgmerchantgrouplabel get(Long id) {
		Tgmerchantgrouplabel t = mapper.get(id);
		return t;
	}
}