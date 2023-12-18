package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.TgmerchantgroupService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
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
public class TgmerchantgroupServiceImpl extends YtBaseServiceImpl<Tgmerchantgroup, Long>
		implements TgmerchantgroupService {
	@Autowired
	private TgmerchantgroupMapper mapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Override
	@Transactional
	public Integer post(Tgmerchantgroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tgmerchantgroup> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tgmerchantgroup>(Collections.emptyList());
			}
		}
		List<Tgmerchantgroup> list = mapper.list(param);
		return new YtPageBean<Tgmerchantgroup>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgmerchantgroup get(Long id) {
		Tgmerchantgroup t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer putmerchant(Tgmerchantgroup t) {
		Merchant mt = merchantmapper.get(t.getMerchantid());
		t.setMerchantname(mt.getName());
		return mapper.put(t);
	}
}