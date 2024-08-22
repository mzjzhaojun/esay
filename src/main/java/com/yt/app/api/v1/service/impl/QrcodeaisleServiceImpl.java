package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.service.QrcodeaisleService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.vo.QrcodeaisleVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */

@Service
public class QrcodeaisleServiceImpl extends YtBaseServiceImpl<Qrcodeaisle, Long> implements QrcodeaisleService {
	@Autowired
	private QrcodeaisleMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaisle t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Qrcodeaisle> list(Map<String, Object> param) {
		List<Qrcodeaisle> list = mapper.list(param);
		return new YtPageBean<Qrcodeaisle>(list);
	}

	@Override
	public Qrcodeaisle get(Long id) {
		Qrcodeaisle t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodeaisleVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaisleVO>(Collections.emptyList());
		}
		List<QrcodeaisleVO> list = mapper.page(param);
		list.forEach(al -> {
			al.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + al.getType()));
		});
		return new YtPageBean<QrcodeaisleVO>(param, list, count);
	}
}