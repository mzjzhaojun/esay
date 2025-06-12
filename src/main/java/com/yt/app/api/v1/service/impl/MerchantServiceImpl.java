package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.IncomeMerchantstatisticalreportsMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutmerchantstatisticalreportsMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.MerchantBot;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.IncomeMerchantstatisticalreports;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Payoutmerchantstatisticalreports;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
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
	private IncomeMapper incomemapper;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private AgentMapper agentmapper;

	@Autowired
	private PayoutMerchantaccountMapper payoutmerchantaccountmapper;

	@Autowired
	private IncomemerchantaccountMapper incomemerchantaccountmapper;

	@Autowired
	private IncomeMerchantstatisticalreportsMapper merchantstatisticalreportsmapper;

	@Autowired
	private PayoutmerchantstatisticalreportsMapper payoutmerchantstatisticalreportsmapper;

	@Autowired
	private MerchantBot merchantbot;

	@Override
	@Transactional
	public Integer post(Merchant t) {
		// user
		User u = new User();
		u.setTenant_id(TenantIdContext.getTenantId());
		u.setUsername(t.getUsername());
		u.setNickname(t.getName());
		u.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_7);
		u.setTwofactorcode(GoogleAuthenticatorUtil.getSecretKey());
		usermapper.postAndTanantId(u);

		//
		t.setUserid(u.getId());
		t.setAppkey(StringUtil.getUUID());
		Integer i = mapper.post(t);

		//
		PayoutMerchantaccount sm = new PayoutMerchantaccount();
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setTowithdrawamount(0.00);
		sm.setToincomeamount(0.00);
		sm.setUserid(u.getId());
		sm.setMerchantid(t.getId());
		sm.setBalance(0.00);
		payoutmerchantaccountmapper.post(sm);

		Incomemerchantaccount ima = new Incomemerchantaccount();
		ima.setTotalincome(0.00);
		ima.setWithdrawamount(0.00);
		ima.setTowithdrawamount(0.00);
		ima.setToincomeamount(0.00);
		ima.setUserid(u.getId());
		ima.setMerchantid(t.getId());
		ima.setBalance(0.00);
		incomemerchantaccountmapper.post(ima);

		return i;
	}

	@Override
	@Transactional
	public Merchant postMerchant(Merchant t) {
		// user
		User u = new User();
		u.setUsername(t.getUsername());
		u.setNickname(t.getName());
		u.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_4);
		u.setTwofactorcode(GoogleAuthenticatorUtil.getSecretKey());
		usermapper.post(u);

		//
		t.setTenant_id(u.getTenant_id());
		t.setUserid(u.getId());
		t.setAppkey(StringUtil.getUUID());
		mapper.post(t);

		//
		PayoutMerchantaccount sm = new PayoutMerchantaccount();
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setTowithdrawamount(0.00);
		sm.setToincomeamount(0.00);
		sm.setUserid(u.getId());
		sm.setMerchantid(t.getId());
		sm.setBalance(0.00);
		payoutmerchantaccountmapper.post(sm);

		Incomemerchantaccount ima = new Incomemerchantaccount();
		ima.setTotalincome(0.00);
		ima.setWithdrawamount(0.00);
		ima.setTowithdrawamount(0.00);
		ima.setToincomeamount(0.00);
		ima.setUserid(u.getId());
		ima.setMerchantid(t.getId());
		ima.setBalance(0.00);
		incomemerchantaccountmapper.post(ima);
		TenantIdContext.remove();
		return t;
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

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Merchant> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Merchant>(Collections.emptyList());
			}
		}
		List<Merchant> list = mapper.list(param);
		return new YtPageBean<Merchant>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchant get(Long id) {
		Merchant t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Merchant t = mapper.get(id);
		payoutmerchantaccountmapper.deleteByUserId(t.getUserid());
		incomemerchantaccountmapper.deleteByUserId(t.getUserid());
		usermapper.delete(t.getUserid());
		return mapper.delete(id);
	}

	@Override
	@Transactional
	public Integer putagent(Merchant m) {
		Integer i = 0;
		Merchant t = mapper.get(m.getId());
		if (m.getAgentid() == null) {
			Agent ag = agentmapper.get(t.getAgentid());
			if (ag != null) {
				ag.setDownmerchantcount(ag.getDownmerchantcount() - 1);
				agentmapper.put(ag);
			}
			i = mapper.removeagent(t.getId());
		} else {
			Agent ag = agentmapper.get(m.getAgentid());
			ag.setDownmerchantcount(ag.getDownmerchantcount() + 1);
			agentmapper.put(ag);
			t.setAgentid(ag.getId());
			t.setAgentname(ag.getName());
			i = mapper.putagent(t);
		}
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchant getData() {
		Merchant t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}

	@Override
	@Transactional
	public void updateIncome(Merchant m, String date) {
		RLock lock = RedissonUtil.getLock(m.getId());
		try {
			lock.lock();
			TenantIdContext.setTenantId(m.getTenant_id());

			// 代收
			IncomeMerchantstatisticalreports msr = new IncomeMerchantstatisticalreports();
			msr.setDateval(date);
			msr.setName(m.getName());
			msr.setBalance(m.getBalance());
			msr.setUserid(m.getUserid());
			msr.setMerchantid(m.getId());
			msr.setTodayincome(m.getTodaycount());
			msr.setIncomecount(m.getCount());
			// 查询每日统计数据
			IncomeVO imaov = incomemapper.countMerchantOrder(m.getId(), date);
			msr.setTodayorder(imaov.getOrdercount());
			msr.setTodayorderamount(imaov.getAmount());
			msr.setTodaysuccessorderamount(imaov.getIncomeamount());

			IncomeVO imaovsuccess = incomemapper.countMerchantSuccessOrder(m.getId(), date);
			msr.setSuccessorder(imaovsuccess.getOrdercount());
			msr.setIncomeuserpaycount(imaovsuccess.getAmount());
			msr.setIncomeuserpaysuccesscount(imaovsuccess.getIncomeamount());
			try {
				if (msr.getSuccessorder() > 0) {
					double successRate = ((double) msr.getSuccessorder() / msr.getTodayorder()) * 100;
					msr.setPayoutrate(successRate);
				}
			} catch (Exception e) {
				msr.setPayoutrate(0.0);
			}
			merchantstatisticalreportsmapper.post(msr);
			// 发送机器人数据
			merchantbot.statisticsMerchant(m);
			// 清空每日数据
			mapper.updatetodayvalue(m.getId());

			TenantIdContext.remove();
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional
	public void updatePayout(Merchant m, String date) {
		RLock lock = RedissonUtil.getLock(m.getId());
		try {
			lock.lock();
			TenantIdContext.setTenantId(m.getTenant_id());

			// 代收
			Payoutmerchantstatisticalreports msr = new Payoutmerchantstatisticalreports();
			msr.setDateval(date);
			msr.setName(m.getName());
			msr.setBalance(m.getBalance());
			msr.setUserid(m.getUserid());
			msr.setMerchantid(m.getId());
			msr.setTodayincome(m.getTodaycount());
			msr.setIncomecount(m.getCount());
			// 查询每日统计数据
			PayoutVO imaov = payoutmapper.countMerchantOrder(m.getId(), date);
			msr.setTodayorder(imaov.getOrdercount());
			msr.setTodayorderamount(imaov.getAmount());
			msr.setTodaysuccessorderamount(imaov.getIncomeamount());

			PayoutVO imaovsuccess = payoutmapper.countMerchantSuccessOrder(m.getId(), date);
			msr.setSuccessorder(imaovsuccess.getOrdercount());
			msr.setIncomeuserpaycount(imaovsuccess.getAmount());
			msr.setIncomeuserpaysuccesscount(imaovsuccess.getIncomeamount());
			try {
				if (msr.getSuccessorder() > 0) {
					double successRate = ((double) msr.getSuccessorder() / msr.getTodayorder()) * 100;
					msr.setPayoutrate(successRate);
				}
			} catch (Exception e) {
				msr.setPayoutrate(0.0);
			}
			payoutmerchantstatisticalreportsmapper.post(msr);
			// 发送机器人数据
			merchantbot.statisticsMerchant(m);
			// 清空每日数据
			mapper.updatetodayvalue(m.getId());

			TenantIdContext.remove();
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}