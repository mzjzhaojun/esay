package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.MerchantaisleMapper;
import com.yt.app.api.v1.service.AisleService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Aisle;
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
 * @version v1 @createdate2023-11-10 19:00:03
 */

@Service
public class AisleServiceImpl extends YtBaseServiceImpl<Aisle, Long> implements AisleService {
	@Autowired
	private AisleMapper mapper;

	@Autowired
	private MerchantaisleMapper merchantaislemapper;

	@Override
	@Transactional
	public Integer post(Aisle t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		// 删除关联表
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		merchantaislemapper.deleteByAisleid(id);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Aisle> page(Map<String, Object> param) {

		if (param.get("merchantid") != null) {
			List<Merchantaisle> listmqas = merchantaislemapper.getByMid(Long.valueOf(param.get("merchantid").toString()));
			if (listmqas.size() > 0) {
				long[] qraids = listmqas.stream().mapToLong(mqa -> mqa.getAisleid()).distinct().toArray();
				param.put("existids", qraids);
			}
		}

		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Aisle>(Collections.emptyList());
			}
		}
		List<Aisle> list = mapper.list(param);
		list.forEach(al -> {
			al.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + al.getType()));
		});
		return new YtPageBean<Aisle>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Aisle get(Long id) {
		Aisle t = mapper.get(id);
		return t;
	}
}