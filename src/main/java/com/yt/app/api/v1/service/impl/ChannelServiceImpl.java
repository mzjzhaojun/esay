package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountMapper;
import com.yt.app.api.v1.mapper.ChannelstatisticalreportsMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Channelstatisticalreports;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Qrcodeaccount;
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

	@Autowired
	private QrcodeaccountMapper qrcodeaccountmapper;

	@Autowired
	private QrcodeaccountorderMapper qrcodeaccountordermapper;

	@Autowired
	private ChannelstatisticalreportsMapper channelstatisticalreportsmapper;

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
		sm.setBalance(0.00);
		channelaccountmapper.post(sm);

		Qrcodeaccount qa = new Qrcodeaccount();
		qa.setTotalincome(0.00);
		qa.setWithdrawamount(0.00);
		qa.setTowithdrawamount(0.00);
		qa.setToincomeamount(0.00);
		qa.setUserid(u.getId());
		qa.setChannelid(t.getId());
		qa.setBalance(0.00);
		qrcodeaccountmapper.post(qa);
		return i;
	}

	@Override

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
	@Transactional
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
		String balance = null;
		switch (cl.getName()) {
		case DictionaryResource.LLAISLE:
			SysTyBalance stb = PayUtil.SendTySelectBalance(cl);
			cl.setRemotebalance(stb.getAvailableBalance());
			break;
		case DictionaryResource.HSAISLE:
			balance = PayUtil.SendHsGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.WDAISLE:
			balance = PayUtil.SendWdGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.RBLAISLE:
			balance = PayUtil.SendRblGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.GZAISLE:
			balance = PayUtil.SendGzGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.WJAISLE:
			balance = PayUtil.SendWjGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.FCAISLE:
			balance = PayUtil.SendFcGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		case DictionaryResource.AKLAISLE:
			balance = PayUtil.SendAklGetBalance(cl);
			if (balance != null)
				cl.setRemotebalance(Double.valueOf(balance));
			break;
		}
		return mapper.put(cl);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Channel getData() {
		Channel t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}

	@Override
	@Transactional
	public void updateIncome(Income t) {
		RLock lock = RedissonUtil.getLock(t.getQrcodeid());
		try {
			lock.lock();
			Channel m = mapper.get(t.getQrcodeid());
			Qrcodeaccount ma = qrcodeaccountmapper.getByUserId(m.getUserid());
			m.setCount(m.getCount() + t.getChannelincomeamount());
			m.setTodaycount(m.getTodaycount() + t.getChannelincomeamount());
			m.setBalance(ma.getBalance());
			mapper.put(m);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional
	public void updateDayValue(Channel c, String date) {
		RLock lock = RedissonUtil.getLock(c.getId());
		try {
			lock.lock();
			TenantIdContext.setTenantId(c.getTenant_id());
			// 插入报表数据
			Channelstatisticalreports csr = new Channelstatisticalreports();
			csr.setDateval(date);
			csr.setName(c.getName());
			csr.setBalance(c.getBalance());
			csr.setUserid(c.getUserid());
			csr.setChannelid(c.getId());
			csr.setTodayincome(c.getTodaycount());
			csr.setIncomecount(c.getCount());
			// 查询每日统计数据
			QrcodeaccountorderVO imaov = qrcodeaccountordermapper.countOrder(c.getUserid());
			csr.setIncomeuserpaycount(imaov.getRealamount());
			csr.setTodayorder(imaov.getOrdercount());
			csr.setTodayorderamount(imaov.getAmount());

			QrcodeaccountorderVO imaovsuccess = qrcodeaccountordermapper.countSuccessOrder(c.getUserid());
			csr.setIncomeuserpaysuccesscount(imaovsuccess.getRealamount());
			csr.setSuccessorder(imaovsuccess.getOrdercount());
			csr.setTodaysuccessorderamount(imaovsuccess.getAmount());
			channelstatisticalreportsmapper.post(csr);

			// 清空每日数据
			mapper.updatetodayvalue(c.getId());
			TenantIdContext.remove();
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}