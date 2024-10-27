package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.AgentaccountrecordMapper;
import com.yt.app.api.v1.service.AgentaccountrecordService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountrecord;
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
 * @version v1 @createdate2023-11-18 12:44:01
 */

@Service
public class AgentaccountrecordServiceImpl extends YtBaseServiceImpl<Agentaccountrecord, Long> implements AgentaccountrecordService {
	@Autowired
	private AgentaccountrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Agentaccountrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Agentaccountrecord> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Agentaccountrecord>(Collections.emptyList());
			}
		}
		List<Agentaccountrecord> list = mapper.list(param);
		list.forEach(maaj -> {
			maaj.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + maaj.getType()));
		});
		return new YtPageBean<Agentaccountrecord>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccountrecord get(Long id) {
		Agentaccountrecord t = mapper.get(id);
		return t;
	}
}