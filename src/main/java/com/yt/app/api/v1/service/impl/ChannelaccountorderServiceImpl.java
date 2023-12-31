package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.ChannelaccountorderService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

@Service
public class ChannelaccountorderServiceImpl extends YtBaseServiceImpl<Channelaccountorder, Long>
		implements ChannelaccountorderService {
	@Autowired
	private ChannelaccountorderMapper mapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Autowired
	private SystemaccountService systemaccountservice;

	@Autowired
	private ChannelaccountService channelaccountservice;

	@Override
	@Transactional
	public Integer post(Channelaccountorder t) {
		Channel m = null;
		if (t.getChannelid() == null) {
			m = channelmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = channelmapper.get(t.getChannelid());
			t.setUserid(m.getUserid());
		}
		// 插入充值记录
		t.setChannelid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNkname());
		t.setChannelcode(m.getCode());
		t.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
		t.setAmountreceived((t.getAmount() * (t.getExchange() + t.getChannelexchange())));
		t.setType(DictionaryResource.ORDERTYPE_20);
		t.setOrdernum("CZQ" + StringUtil.getOrderNum());
		t.setRemark("充值金额：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 收入账户和记录
		channelaccountservice.totalincome(t);

		//
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Channelaccountorder> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Channelaccountorder>(Collections.emptyList());
			}
		}
		List<Channelaccountorder> list = mapper.list(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		return new YtPageBean<Channelaccountorder>(param, list, count);
	}

	@Override
	public Channelaccountorder get(Long id) {
		Channelaccountorder t = mapper.get(id);
		return t;
	}

//////////////////////////////////////////////////////////////收入
	@Override
	@Transactional
	public Integer pass(Long id) {
//
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
//
		channelaccountservice.updateTotalincome(mao);
//
		return i;
	}

	@Override
	@Transactional
	public Integer turndown(Long id) {
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
//
		channelaccountservice.turndownTotalincome(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancle(Long id) {
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
//
		channelaccountservice.cancleTotalincome(mao);
		return i;
	}

//////////////////////////////////////////////////支出 /////////////////////////

	@Override
	@Transactional
	public Integer passWithdraw(Long id) {
//
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_11);
		Integer i = mapper.put(mao);
//
		channelaccountservice.updateWithdrawamount(mao);
//
		systemaccountservice.updateWithdrawamount(mao);
//
		return i;
	}

	@Override
	@Transactional
	public Integer turndownWithdraw(Long id) {
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
		Integer i = mapper.put(mao);
//
		channelaccountservice.turndownWithdrawamount(mao);
		return i;
	}

	@Override
	@Transactional
	public Integer cancleWithdraw(Long id) {
		Channelaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_13);
		Integer i = mapper.put(mao);
//
		channelaccountservice.cancleWithdrawamount(mao);
		return i;
	}

}