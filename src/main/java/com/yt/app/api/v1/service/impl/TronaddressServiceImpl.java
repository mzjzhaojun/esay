package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronaddressMapper;
import com.yt.app.api.v1.service.TronaddressService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tronaddress;
import com.yt.app.api.v1.vo.TronaddressVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 01:44:43
 */

@Service
public class TronaddressServiceImpl extends YtBaseServiceImpl<Tronaddress, Long> implements TronaddressService {
	@Autowired
	private TronaddressMapper mapper;

	@Override
	@Transactional
	public Integer post(Tronaddress t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tronaddress get(Long id) {
		Tronaddress t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TronaddressVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TronaddressVO>(Collections.emptyList());
		}
		List<TronaddressVO> list = mapper.page(param);
		return new YtPageBean<TronaddressVO>(param, list, count);
	}
}