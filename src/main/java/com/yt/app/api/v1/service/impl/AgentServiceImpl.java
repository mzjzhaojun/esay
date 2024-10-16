package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.AgentService;

import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.RedissonUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */

@Service
public class AgentServiceImpl extends YtBaseServiceImpl<Agent, Long> implements AgentService {
	@Autowired
	private AgentMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private AgentaccountMapper agentaccountmapper;

	@Override
	@Transactional
	public Integer post(Agent t) {
		// user
		User u = new User();
		u.setTenant_id(TenantIdContext.getTenantId());
		u.setUsername(t.getUsername());
		u.setNickname(t.getName());
		u.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_5);
		u.setTwofactorcode(GoogleAuthenticatorUtil.getSecretKey());
		usermapper.postAndTanantId(u);

		t.setUserid(u.getId());
		Integer i = mapper.post(t);

		//
		Agentaccount sm = new Agentaccount();
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setTowithdrawamount(0.00);
		sm.setToincomeamount(0.00);
		sm.setUserid(u.getId());
		sm.setAgentid(t.getId());
		sm.setBalance(0.00);
		agentaccountmapper.post(sm);

		return i;
	}

	@Override
	@Transactional
	public Integer put(Agent t) {
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

	@Override

	public YtIPage<Agent> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Agent>(Collections.emptyList());
			}
		}
		List<Agent> list = mapper.list(param);
		return new YtPageBean<Agent>(param, list, count);
	}

	@Override

	public Agent get(Long id) {
		Agent t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Agent t = mapper.get(id);
		usermapper.delete(t.getUserid());
		return mapper.delete(id);
	}

	@Override
	@Transactional
	public void updateWithdraw(Agentaccount t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agent a = mapper.get(t.getAgentid());
			a.setBalance(t.getBalance());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional
	public void updatePayout(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agent a = mapper.get(t.getAgentid());
			a.setBalance(a.getBalance() + t.getAgentincome());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional
	public void updateIncome(Agentaccount t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agent a = mapper.get(t.getAgentid());
			a.setBalance(t.getBalance());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void updateExchange(Exchange t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agent a = mapper.get(t.getAgentid());
			a.setBalance(a.getBalance() + t.getAgentincome());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}