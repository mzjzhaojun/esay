package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SystemcapitalrecordMapper;
import com.yt.app.api.v1.service.SystemcapitalrecordService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Systemcapitalrecord;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */

@Service
public class SystemcapitalrecordServiceImpl extends YtBaseServiceImpl<Systemcapitalrecord, Long>
		implements SystemcapitalrecordService {
	@Autowired
	private SystemcapitalrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Systemcapitalrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Systemcapitalrecord> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Systemcapitalrecord> list = mapper.list(param);
		list.forEach(maaj -> {
			maaj.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + maaj.getType()));
		});
		return new YtPageBean<Systemcapitalrecord>(param, list, count);
	}

	@Override
	public Systemcapitalrecord get(Long id) {
		Systemcapitalrecord t = mapper.get(id);
		return t;
	}
}