package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantcustomerbanksMapper;
import com.yt.app.api.v1.mapper.SysbankMapper;
import com.yt.app.api.v1.service.MerchantcustomerbanksService;

import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Merchantcustomerbanks;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Sysbank;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 18:43:33
 */

@Service
public class MerchantcustomerbanksServiceImpl extends YtBaseServiceImpl<Merchantcustomerbanks, Long> implements MerchantcustomerbanksService {
	@Autowired
	private MerchantcustomerbanksMapper mapper;

	@Autowired
	private SysbankMapper sysbankmapper;

	@Override
	@Transactional
	public Integer post(Merchantcustomerbanks t) {
		t.setUserid(SysUserContext.getUserId());
		if (t.getBankname() == null) {
			Sysbank sb = sysbankmapper.getByCode(t.getBankcode());
			if (sb != null) {
				t.setBankname(sb.getName());
			}
		}
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@Transactional
	public Integer add(Payout t) {
		Merchantcustomerbanks mccb = mapper.getByAccNumber(t.getAccnumer());
		if (mccb == null) {
			mccb = new Merchantcustomerbanks();
			mccb.setUserid(t.getUserid());
			mccb.setAccname(t.getAccname());
			mccb.setAccnumber(t.getAccnumer());
			mccb.setBankcode(t.getBankcode());
			mccb.setBankname(t.getBankname());
			mccb.setBankaddress(t.getBankaddress());
			Integer i = mapper.post(mccb);
			return i;
		}
		return 0;
	}

	@Override
	@Transactional
	public Integer add(Exchange t) {
		Merchantcustomerbanks mccb = mapper.getByAccNumber(t.getAccnumer());
		if (mccb == null) {
			mccb = new Merchantcustomerbanks();
			mccb.setUserid(t.getUserid());
			mccb.setAccname(t.getAccname());
			mccb.setAccnumber(t.getAccnumer());
			mccb.setBankcode(t.getBankcode());
			mccb.setBankname(t.getBankname());
			mccb.setBankaddress(t.getBankaddress());
			Integer i = mapper.post(mccb);
			return i;
		}
		return 0;
	}

	@Override

	public YtIPage<Merchantcustomerbanks> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Merchantcustomerbanks>(Collections.emptyList());
			}
		}
		List<Merchantcustomerbanks> list = mapper.list(param);
		list.forEach(t -> {
			t.setValue(t.getAccname());
		});
		return new YtPageBean<Merchantcustomerbanks>(param, list, count);
	}

	@Override

	public Merchantcustomerbanks get(Long id) {
		Merchantcustomerbanks t = mapper.get(id);
		return t;
	}
}