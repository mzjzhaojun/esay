package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.api.v1.service.TronmemberorderService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.vo.TronmemberorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-10-15 00:23:49
 */

@Service
public class TronmemberorderServiceImpl extends YtBaseServiceImpl<Tronmemberorder, Long> implements TronmemberorderService {
	@Autowired
	private TronmemberorderMapper mapper;

	@Override
	@Transactional
	public Integer post(Tronmemberorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tronmemberorder get(Long id) {
		Tronmemberorder t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TronmemberorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TronmemberorderVO>(Collections.emptyList());
		}
		List<TronmemberorderVO> list = mapper.page(param);
		return new YtPageBean<TronmemberorderVO>(param, list, count);
	}
}