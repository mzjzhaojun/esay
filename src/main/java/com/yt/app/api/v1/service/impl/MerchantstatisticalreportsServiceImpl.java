package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantstatisticalreportsMapper;
import com.yt.app.api.v1.service.MerchantstatisticalreportsService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantstatisticalreports;
import com.yt.app.api.v1.vo.MerchantstatisticalreportsVO;
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
public class MerchantstatisticalreportsServiceImpl extends YtBaseServiceImpl<Merchantstatisticalreports, Long>
		implements MerchantstatisticalreportsService {
	@Autowired
	private MerchantstatisticalreportsMapper mapper;

	@Override
	@Transactional
	public Integer post(Merchantstatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Merchantstatisticalreports> list(Map<String, Object> param) {
		List<Merchantstatisticalreports> list = mapper.list(param);
		return new YtPageBean<Merchantstatisticalreports>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchantstatisticalreports get(Long id) {
		Merchantstatisticalreports t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<MerchantstatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<MerchantstatisticalreportsVO>(Collections.emptyList());
		}
		List<MerchantstatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<MerchantstatisticalreportsVO>(param, list, count);
	}
}