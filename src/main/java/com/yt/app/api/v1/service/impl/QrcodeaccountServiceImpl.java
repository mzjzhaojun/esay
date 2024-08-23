package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeaccountMapper;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.vo.QrcodeaccountVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

@Service
public class QrcodeaccountServiceImpl extends YtBaseServiceImpl<Qrcodeaccount, Long> implements QrcodeaccountService {
	@Autowired
	private QrcodeaccountMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Qrcodeaccount> list(Map<String, Object> param) {
		List<Qrcodeaccount> list = mapper.list(param);
		return new YtPageBean<Qrcodeaccount>(list);
	}

	@Override
	public Qrcodeaccount get(Long id) {
		Qrcodeaccount t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodeaccountVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountVO>(Collections.emptyList());
		}
		List<QrcodeaccountVO> list = mapper.page(param);
		return new YtPageBean<QrcodeaccountVO>(param, list, count);
	}
}