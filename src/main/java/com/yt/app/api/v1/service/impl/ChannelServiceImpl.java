package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

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

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Channel> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Channel> list = mapper.list(param);
		return new YtPageBean<Channel>(param, list, count);
	}

	@Override
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
	public String sendChannel(Channel cl) {
		/// HttpHeaders headers = new HttpHeaders();
		// headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)
		/// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		// HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
		// RestTemplate resttemplate = new RestTemplate();
		// ResponseEntity<SysOxxVo> sov = resttemplate.exchange(cl.getApiip(),
		/// HttpMethod.GET, httpEntity, SysOxxVo.class);
		// SysOxxVo data = sov.getBody();
		// List<Object> list = data.getData().getSell();
		return "C" + StringUtil.getOrderNum();
	}

	@Override
	@Transactional
	public void updatePayout(Payout t) {
		Channel c = mapper.get(t.getMerchantid());
		RLock lock = RedissonUtil.getLock(c.getId());
		try {
			lock.lock();
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
		Channel a = mapper.get(t.getChannelid());
		RLock lock = RedissonUtil.getLock(a.getId());
		try {
			lock.lock();
			a.setBalance(t.getBalance());
			mapper.put(a);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}