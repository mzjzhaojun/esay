package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.PayconfigMapper;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

@Service
public class PayconfigServiceImpl extends YtBaseServiceImpl<Payconfig, Long> implements PayconfigService {
	@Autowired
	private PayconfigMapper mapper;

	@Override
	@Transactional
	public Integer post(Payconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Payconfig> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Payconfig> list = mapper.list(param);
		return new YtPageBean<Payconfig>(param, list, count);
	}

	@Override
	public Payconfig get(Long id) {
		Payconfig t = mapper.get(id);
		return t;
	}

	@Override
	public Payconfig getData() {
		return mapper.get(ServiceConstant.SYSTEM_PAYCONFIG_EXCHANGE);
	}
}