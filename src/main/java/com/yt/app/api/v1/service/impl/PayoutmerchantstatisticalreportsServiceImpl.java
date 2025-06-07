package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.PayoutmerchantstatisticalreportsMapper;
import com.yt.app.api.v1.service.PayoutmerchantstatisticalreportsService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Payoutmerchantstatisticalreports;
import com.yt.app.api.v1.vo.PayoutmerchantstatisticalreportsVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-06-07 23:16:10
 */

@Service
public class PayoutmerchantstatisticalreportsServiceImpl extends YtBaseServiceImpl<Payoutmerchantstatisticalreports, Long> implements PayoutmerchantstatisticalreportsService {
	@Autowired
	private PayoutmerchantstatisticalreportsMapper mapper;

	@Override
	@Transactional
	public Integer post(Payoutmerchantstatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Payoutmerchantstatisticalreports get(Long id) {
		Payoutmerchantstatisticalreports t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<PayoutmerchantstatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<PayoutmerchantstatisticalreportsVO>(Collections.emptyList());
		}
		List<PayoutmerchantstatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<PayoutmerchantstatisticalreportsVO>(param, list, count);
	}
}