package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.api.v1.service.PayoutMerchantaccountorderService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.User;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class PayoutMerchantaccountorderServiceImpl extends YtBaseServiceImpl<PayoutMerchantaccountorder, Long> implements PayoutMerchantaccountorderService {
	@Autowired
	private PayoutMerchantaccountorderMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private MerchantaccountbankMapper merchantaccountbankmapper;

	@Autowired
	private PayoutMerchantaccountService merchantaccountservice;

	/**
	 * 充值
	 */
	@Override
	public Integer post(PayoutMerchantaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1");
		}
		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}
		// 收入订单
		t.setUsdtval(t.getAmount());
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setAmountreceived((t.getAmount() * (t.getExchange() + t.getMerchantexchange())));
		t.setType(DictionaryResource.ORDERTYPE_11);
		t.setOrdernum("MT" + StringUtil.getOrderNum());
		t.setRemark("商户充值￥：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 收入账户和记录
		merchantaccountservice.totalincome(t);
		//
		return i;
	}

	// 代付充值成功
	@Override
	public void incomemanual(PayoutMerchantaccountorder mco) {
		RLock lock = RedissonUtil.getLock(mco.getId());
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		try {
			lock.lock();
			PayoutMerchantaccountorder mao = mapper.get(mco.getId());
			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				mao.setStatus(mco.getStatus());
				Integer i = mapper.put(mao);
				if (i > 0) {
					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
						//
						merchantaccountservice.updateTotalincome(mao);
						//
					} else {
						merchantaccountservice.turndownTotalincome(mao);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现
	@Override
	public Integer save(PayoutMerchantaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1");
		}
		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}
		Merchantaccountbank mab = merchantaccountbankmapper.get(Long.valueOf(t.getAccnumber()));
		t.setAccname(mab.getAccname());
		t.setAccnumber(mab.getAccnumber());
		// 支出订单
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setExchange(t.getMerchantexchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getMerchantexchange());
		t.setType(DictionaryResource.ORDERTYPE_15);
		t.setOrdernum("MW" + StringUtil.getOrderNum());
		t.setRemark("商户代付提现￥：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		merchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	/**
	 * app提现
	 */
	@Override
	public Integer appsave(PayoutMerchantaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1");
		}
		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}

		t.setAccname("App提现");
		// 直接提到usdt地址
		// 支出订单
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setExchange(t.getMerchantexchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getMerchantexchange());
		t.setType(DictionaryResource.ORDERTYPE_15);
		t.setOrdernum("MW" + StringUtil.getOrderNum());
		t.setRemark("商户代付提现￥：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		merchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<PayoutMerchantaccountorder> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<PayoutMerchantaccountorder>(Collections.emptyList());
			}
		}
		List<PayoutMerchantaccountorder> list = mapper.list(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		return new YtPageBean<PayoutMerchantaccountorder>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayoutMerchantaccountorder get(Long id) {
		PayoutMerchantaccountorder t = mapper.get(id);
		return t;
	}

	/**
	 * 充值取消
	 */
	@Override
	public Integer cancle(Long id) {
		RLock lock = RedissonUtil.getLock(id);
		try {
			lock.lock();
			PayoutMerchantaccountorder mao = mapper.get(id);
			mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
			Integer i = mapper.put(mao);
			//
			merchantaccountservice.cancleTotalincome(mao);
			return i;
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return 0;
	}

//////////////////////////////////////////////////////////////提现处理

	@Override
	public void withdrawmanual(PayoutMerchantaccountorder mco) {
		RLock lock = RedissonUtil.getLock(mco.getId());
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		try {
			lock.lock();
			PayoutMerchantaccountorder mao = mapper.get(mco.getId());
			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				mao.setStatus(mco.getStatus());
				mao.setImgurl(mco.getImgurl());
				Integer i = mapper.put(mao);
				if (i > 0) {
					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
						//
						merchantaccountservice.updateWithdrawamount(mao);
						//
					} else {
						merchantaccountservice.turndownWithdrawamount(mao);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 提现取消
	 */
	@Override
	public Integer cancleWithdraw(Long id) {
		RLock lock = RedissonUtil.getLock(id);
		try {
			lock.lock();
			PayoutMerchantaccountorder mao = mapper.get(id);
			mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
			Integer i = mapper.put(mao);
			//
			merchantaccountservice.cancleWithdrawamount(mao);
			return i;
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return 0;
	}

}