package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */

@Service
public class MerchantServiceImpl extends YtBaseServiceImpl<Merchant, Long> implements MerchantService {
	@Autowired
	private MerchantMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private AgentMapper agentmapper;

	@Autowired
	private MerchantaccountMapper merchantaccountmapper;

	@Override
	@Transactional
	public Integer post(Merchant t) {
		// user
		User u = new User();
		u.setTenant_id(TenantIdContext.getTenantId());
		u.setUsername(t.getUsername());
		u.setNickname(t.getName());
		u.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_4);
		usermapper.postAndTanantId(u);

		//
		t.setUserid(u.getId());
		t.setAppkey(StringUtil.getNonceStr(32));
		Integer i = mapper.post(t);

		//
		Merchantaccount sm = new Merchantaccount();
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setTowithdrawamount(0.00);
		sm.setToincomeamount(0.00);
		sm.setUserid(u.getId());
		sm.setMerchantid(t.getId());
		merchantaccountmapper.post(sm);

		return i;
	}

	@Override
	@Transactional
	public Integer put(Merchant t) {
		Integer i = 0;
		if (t.getPassword() != null) {
			User u = usermapper.get(t.getUserid());
			u.setPassword(PasswordUtil.encodePassword(t.getPassword()));
			usermapper.put(u);
		}
		i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Merchant> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Merchant> list = mapper.list(param);
		return new YtPageBean<Merchant>(param, list, count);
	}

	@Override
	public Merchant get(Long id) {
		Merchant t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Merchant t = mapper.get(id);
		usermapper.delete(t.getUserid());
		return mapper.delete(id);
	}

	@Override
	public Integer removeagent(Long id) {
		Integer i = mapper.removeagent(id);
		return i;
	}

	@Override
	public Integer putagent(Merchant m) {
		Integer i = 0;
		Merchant t = mapper.get(m.getId());
		Agent ag = agentmapper.get(m.getAgentid());
		ag.setDownmerchantcount(ag.getDownmerchantcount() + 1);
		agentmapper.put(ag);
		t.setAgentname(ag.getName());
		i = mapper.putagent(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	public void updateInCome(Merchantaccount ma) {
		Merchant m = mapper.get(ma.getMerchantid());
		RLock lock = RedissonUtil.getLock(m.getId());
		try {
			lock.lock();
			m.setBalance(ma.getBalance());
			mapper.put(m);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void updatePayout(Payout t) {
		Merchant m = mapper.get(t.getMerchantid());
		Merchantaccount ma = merchantaccountmapper.getByUserId(m.getUserid());
		RLock lock = RedissonUtil.getLock(m.getId());
		try {
			lock.lock();
			m.setCount(m.getCount() + t.getAmount());// 总量不包含手续费和交易费
			m.setTodaycount(m.getTodaycount() + t.getAmount());
			m.setTodaycost(m.getTodaycost() + t.getMerchantcost());
			m.setBalance(ma.getBalance());
			mapper.put(m);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Merchant getData() {
		Merchant t = mapper.getByUserId(JwtUserContext.get().getUserId());
		return t;
	}
}