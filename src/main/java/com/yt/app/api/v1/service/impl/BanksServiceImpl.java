package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.BanksMapper;
import com.yt.app.api.v1.service.BanksService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Banks;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Snowflake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-19 13:11:56
 */

@Service
public class BanksServiceImpl extends YtBaseServiceImpl<Banks, Long> implements BanksService {
	@Autowired
	private BanksMapper mapper;

	@Autowired
	Snowflake sfe;

	@Override
	@Transactional
	public Integer post(Banks t) {
		t.setUserid(SysUserContext.getUserId());
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Banks> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Banks>(Collections.emptyList());
			}
		}
		List<Banks> list = mapper.list(param);
		list.forEach(t -> {
			t.setValue(t.getAccname());
		});
		return new YtPageBean<Banks>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Banks get(Long id) {
		Banks t = mapper.get(id);
		return t;
	}

	@Override
	public void initdata() {
		ArrayList<Banks> list = new ArrayList<Banks>();
		for (Integer i = 0; i < 2000000; i++) {
			Banks e = new Banks();
			e.setId(sfe.nextId());
			e.setTenant_id(1720395906240614400L);
			e.setUserid(1724702879513710592L);
			e.setAccname(StringUtil.getRandomBoyName());
			e.setAccnumber(StringUtil.getNonceStr(10));
			e.setBankname("中国银行");
			list.add(e);
			if (i % 1000 == 0) {
				mapper.batchSava(list);
				list = new ArrayList<Banks>();
			}
		}

	}
}