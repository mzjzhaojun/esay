package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.QrcodeaccountorderService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:07:27
 */

@Service
public class QrcodeaccountorderServiceImpl extends YtBaseServiceImpl<Qrcodeaccountorder, Long>
		implements QrcodeaccountorderService {
	@Autowired
	private QrcodeaccountorderMapper mapper;

	@Autowired
	private IncomemerchantaccountorderMapper incomemerchantaccountordermapper;

	@Autowired
	private IncomeMapper incomemapper;

	@Autowired
	private QrcodeaccountService qrcodeaccountservice;

	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;

	@Override
	@Transactional
	public Integer post(Qrcodeaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@Transactional
	public Integer put(Qrcodeaccountorder t) {
		Integer i = 0;
		Income income = incomemapper.getByQrcodeOrderNum(t.getOrdernum());
		if (income.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
			Qrcodeaccountorder qrcodeaccountorder = mapper.get(t.getId());
			income.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			if (income.getNotifystatus() == DictionaryResource.PAYOUTNOTIFYSTATUS_61)
				income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			//
			incomemapper.put(income);
			//
			qrcodeaccountservice.updateTotalincome(qrcodeaccountorder);

			Incomemerchantaccountorder incomemerchantaccountorder = incomemerchantaccountordermapper
					.getByOrderNum(income.getMerchantorderid());
			//
			incomemerchantaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			incomemerchantaccountordermapper.put(incomemerchantaccountorder);
			//
			incomemerchantaccountservice.updateTotalincome(incomemerchantaccountorder);
			//
			qrcodeaccountorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
			i = mapper.put(qrcodeaccountorder);
		}
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	public YtIPage<Qrcodeaccountorder> list(Map<String, Object> param) {
		List<Qrcodeaccountorder> list = mapper.list(param);
		return new YtPageBean<Qrcodeaccountorder>(list);
	}

	@Override
	public Qrcodeaccountorder get(Long id) {
		Qrcodeaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodeaccountorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountorderVO>(Collections.emptyList());
		}
		List<QrcodeaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<QrcodeaccountorderVO>(param, list, count);
	}
}