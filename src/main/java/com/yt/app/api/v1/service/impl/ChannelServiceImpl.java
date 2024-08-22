package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */

@Service
public class ChannelServiceImpl extends YtBaseServiceImpl<Channel, Long> implements ChannelService {
	@Autowired
	private ChannelMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private ChannelaccountMapper channelaccountmapper;

	@Override
	@Transactional
	public Integer post(Channel t) {

		User u = new User();
		u.setTenant_id(TenantIdContext.getTenantId());
		u.setUsername(t.getName());
		u.setNickname(t.getName());
		u.setPassword(PasswordUtil.encodePassword(AppConstant.DEFAULT_CONTEXT_KEY_PASSWORD));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_6);
		u.setTwofactorcode(GoogleAuthenticatorUtil.getSecretKey());
		usermapper.postAndTanantId(u);

		t.setUserid(u.getId());
		Integer i = mapper.post(t);

		//
		Channelaccount sm = new Channelaccount();
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setTowithdrawamount(0.00);
		sm.setToincomeamount(0.00);
		sm.setUserid(u.getId());
		sm.setChannelid(t.getId());
		channelaccountmapper.post(sm);

		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Channel> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Channel>(Collections.emptyList());
			}
		}
		List<Channel> list = mapper.list(param);
		list.forEach(al -> {
			al.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + al.getType()));
		});
		return new YtPageBean<Channel>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Channel get(Long id) {
		Channel t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Channel t = mapper.get(id);
		usermapper.delete(t.getUserid());
		return mapper.delete(id);
	}

	@Override
	@Transactional
	public void updatePayout(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channel c = mapper.get(t.getChannelid());
			c.setBalance(c.getBalance() - t.getChannelpay());
			mapper.put(c);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional
	public void updateIncome(Channelaccount t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channel a = mapper.get(t.getChannelid());
			a.setBalance(t.getBalance());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void withdrawamount(Channelaccount t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channel a = mapper.get(t.getChannelid());
			a.setBalance(t.getBalance());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void updateExchange(Exchange t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channel c = mapper.get(t.getChannelid());
			c.setBalance(c.getBalance() - t.getChannelpay());
			mapper.put(c);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Integer getRemotebalance(Long id) {
		Channel cl = mapper.get(id);
		SysTyBalance stb = PayUtil.SendTySelectBalance(cl);
		cl.setRemotebalance(stb.getAvailableBalance());
		return mapper.put(cl);
	}
}