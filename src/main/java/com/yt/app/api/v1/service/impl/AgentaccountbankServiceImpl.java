package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.AgentaccountbankMapper;
import com.yt.app.api.v1.service.AgentaccountbankService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountbank;
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
 * @version v1 @createdate2023-11-16 10:39:42
 */

@Service
public class AgentaccountbankServiceImpl extends YtBaseServiceImpl<Agentaccountbank, Long> implements AgentaccountbankService {
	@Autowired
	private AgentaccountbankMapper mapper;

	@Override
	@Transactional
	public Integer post(Agentaccountbank t) {
		t.setUserid(SysUserContext.getUserId());
		t.setUsername(SysUserContext.getUsername());
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Agentaccountbank> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Agentaccountbank>(Collections.emptyList());
			}
		}
		List<Agentaccountbank> list = mapper.list(param);
		list.forEach(mab -> {
			mab.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mab.getType()));
		});
		return new YtPageBean<Agentaccountbank>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccountbank get(Long id) {
		Agentaccountbank t = mapper.get(id);
		return t;
	}
}