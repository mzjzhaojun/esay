package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.service.MerchantaisleService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:15:12
 */

@Service
public class MerchantaisleServiceImpl extends YtBaseServiceImpl<Merchantaisle, Long> implements MerchantaisleService {
	@Autowired
	private MerchantaisleMapper mapper;

	@Override
	@Transactional
	public Integer post(Merchantaisle t) {
		Merchantaisle m = mapper.getByMidAid(t.getAisleid(), t.getMerchantid());
		Assert.isNull(m, "已经存在的通道");
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Merchantaisle> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Merchantaisle>(Collections.emptyList());
			}
		}
		List<Merchantaisle> list = mapper.list(param);
		list.forEach(al -> {
			al.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + al.getType()));
		});
		return new YtPageBean<Merchantaisle>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchantaisle get(Long id) {
		Merchantaisle t = mapper.get(id);
		return t;
	}
}