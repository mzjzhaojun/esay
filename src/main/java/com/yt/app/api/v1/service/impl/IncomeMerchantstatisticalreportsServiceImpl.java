package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomeMerchantstatisticalreportsMapper;
import com.yt.app.api.v1.service.IncomeMerchantstatisticalreportsService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.IncomeMerchantstatisticalreports;
import com.yt.app.api.v1.vo.IncomeMerchantstatisticalreportsVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:01:51
 */

@Service
public class IncomeMerchantstatisticalreportsServiceImpl extends YtBaseServiceImpl<IncomeMerchantstatisticalreports, Long> implements IncomeMerchantstatisticalreportsService {
	@Autowired
	private IncomeMerchantstatisticalreportsMapper mapper;

	@Override
	@Transactional
	public Integer post(IncomeMerchantstatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public IncomeMerchantstatisticalreports get(Long id) {
		IncomeMerchantstatisticalreports t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<IncomeMerchantstatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomeMerchantstatisticalreportsVO>(Collections.emptyList());
		}
		List<IncomeMerchantstatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<IncomeMerchantstatisticalreportsVO>(param, list, count);
	}
}