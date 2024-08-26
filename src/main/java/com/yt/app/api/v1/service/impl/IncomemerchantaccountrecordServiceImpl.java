package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomemerchantaccountrecordMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountrecordService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Incomemerchantaccountrecord;
import com.yt.app.api.v1.vo.IncomemerchantaccountrecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

@Service
public class IncomemerchantaccountrecordServiceImpl extends YtBaseServiceImpl<Incomemerchantaccountrecord, Long>
		implements IncomemerchantaccountrecordService {
	@Autowired
	private IncomemerchantaccountrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Incomemerchantaccountrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Incomemerchantaccountrecord> list(Map<String, Object> param) {
		List<Incomemerchantaccountrecord> list = mapper.list(param);
		return new YtPageBean<Incomemerchantaccountrecord>(list);
	}

	@Override
	public Incomemerchantaccountrecord get(Long id) {
		Incomemerchantaccountrecord t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<IncomemerchantaccountrecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomemerchantaccountrecordVO>(Collections.emptyList());
		}
		List<IncomemerchantaccountrecordVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<IncomemerchantaccountrecordVO>(param, list, count);
	}
}