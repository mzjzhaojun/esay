package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.service.IncomeService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

@Service
public class IncomeServiceImpl extends YtBaseServiceImpl<Income, Long> implements IncomeService {
	@Autowired
	private IncomeMapper mapper;

	@Override
	@Transactional
	public Integer post(Income t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Income> list(Map<String, Object> param) {
		List<Income> list = mapper.list(param);
		return new YtPageBean<Income>(list);
	}

	@Override
	public Income get(Long id) {
		Income t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<IncomeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomeVO>(Collections.emptyList());
		}
		List<IncomeVO> list = mapper.page(param);
		return new YtPageBean<IncomeVO>(param, list, count);
	}
}