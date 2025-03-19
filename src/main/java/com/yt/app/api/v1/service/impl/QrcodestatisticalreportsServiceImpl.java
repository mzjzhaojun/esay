package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodestatisticalreportsMapper;
import com.yt.app.api.v1.service.QrcodestatisticalreportsService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodestatisticalreports;
import com.yt.app.api.v1.vo.QrcodestatisticalreportsVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 19:51:13
 */

@Service
public class QrcodestatisticalreportsServiceImpl extends YtBaseServiceImpl<Qrcodestatisticalreports, Long> implements QrcodestatisticalreportsService {
	@Autowired
	private QrcodestatisticalreportsMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodestatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Qrcodestatisticalreports get(Long id) {
		Qrcodestatisticalreports t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodestatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodestatisticalreportsVO>(Collections.emptyList());
		}
		List<QrcodestatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<QrcodestatisticalreportsVO>(param, list, count);
	}
}