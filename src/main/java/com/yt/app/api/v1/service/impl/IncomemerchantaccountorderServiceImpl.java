package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountorderService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

@Service
public class IncomemerchantaccountorderServiceImpl extends YtBaseServiceImpl<Incomemerchantaccountorder, Long>
		implements IncomemerchantaccountorderService {
	@Autowired
	private IncomemerchantaccountorderMapper mapper;

	@Override
	@Transactional
	public Integer post(Incomemerchantaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Incomemerchantaccountorder> list(Map<String, Object> param) {
		List<Incomemerchantaccountorder> list = mapper.list(param);
		return new YtPageBean<Incomemerchantaccountorder>(list);
	}

	@Override
	public Incomemerchantaccountorder get(Long id) {
		Incomemerchantaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<IncomemerchantaccountorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomemerchantaccountorderVO>(Collections.emptyList());
		}
		List<IncomemerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<IncomemerchantaccountorderVO>(param, list, count);
	}
}