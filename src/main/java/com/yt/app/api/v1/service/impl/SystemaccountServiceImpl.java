package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.mapper.SystemaccountrecordMapper;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.entity.Systemaccountrecord;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.RedissonUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */

@Service
public class SystemaccountServiceImpl extends YtBaseServiceImpl<Systemaccount, Long> implements SystemaccountService {
	@Autowired
	private SystemaccountMapper mapper;

	@Autowired
	private SystemaccountrecordMapper systemcapitalrecordmapper;

	@Override
	@Transactional
	public Integer post(Systemaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Systemaccount> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Systemaccount>(Collections.emptyList());
			}
		}
		List<Systemaccount> list = mapper.list(param);
		return new YtPageBean<Systemaccount>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Systemaccount get(Long id) {
		Systemaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Systemaccount getData() {
		Systemaccount t = mapper.getByUserId(JwtUserContext.get().getUserId());
		return t;
	}

	/**
	 * 代付充值成功
	 */
	@Override
	public synchronized void updateTotalincome(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_20);
			scr.setUsdtpretotalincome(t.getUsdttotalincome());// 总收入
			scr.setUsdtposttotalincome(t.getUsdttotalincome() + mao.getAmountreceived());// 待确认收入
			scr.setUsdtamount(mao.getAmountreceived());// 操作金额
			t.setUsdttotalincome(t.getUsdttotalincome() + mao.getAmountreceived());
			t.setUsdtbalance(t.getUsdttotalincome() - t.getUsdtwithdrawamount());
			mapper.put(t);
			scr.setUsdtbalance(t.getUsdtbalance());//
			scr.setRemark("系统代付收入金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 商戶代付支出
	 */
	@Override
	public synchronized void updateWithdrawamount(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_21);
			scr.setUsdtprewithdrawamount(t.getUsdtwithdrawamount());
			scr.setUsdtpostwithdrawamount(t.getUsdtwithdrawamount() + mao.getAmountreceived());
			scr.setUsdtamount(mao.getAmountreceived());
			t.setUsdtwithdrawamount(t.getUsdtwithdrawamount() + mao.getAmountreceived());
			t.setUsdtbalance(t.getUsdttotalincome() - t.getUsdtwithdrawamount());
			mapper.put(t);
			scr.setUsdtbalance(t.getUsdtbalance());//
			scr.setRemark("系统代付提现金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 商户代付成功
	 */
	@Override
	public synchronized void updatePayout(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_22);
			scr.setUsdtprewithdrawamount(t.getUsdtwithdrawamount());
			scr.setUsdtpostwithdrawamount(t.getUsdtwithdrawamount() + mao.getAmountreceived());
			scr.setUsdtamount(mao.getAmountreceived());

			t.setUsdtwithdrawamount(t.getUsdtwithdrawamount() + mao.getAmountreceived());
			t.setUsdtbalance(t.getUsdttotalincome() - t.getUsdtwithdrawamount());
			mapper.put(t);
			scr.setUsdtbalance(t.getUsdtbalance());//
			scr.setRemark("系统代付金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 代付代理支出
	 */
	@Override
	public synchronized void updateWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_26);
			scr.setUsdtprewithdrawamount(t.getWithdrawamount());
			scr.setUsdtpostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setUsdtamount(mao.getAmountreceived());
			t.setUsdtwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			t.setUsdtbalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setUsdtbalance(t.getBalance());//
			scr.setRemark("代理代付提现金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 代收成功收款
	@Override
	public synchronized void updateIncome(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getMerchantname());
			scr.setType(DictionaryResource.ORDERTYPE_27);
			scr.setPretotalincome(t.getTotalincome());
			scr.setPosttotalincome(t.getTotalincome() + mao.getIncomeamount());
			scr.setAmount(mao.getIncomeamount());
			t.setTotalincome(t.getTotalincome() + mao.getIncomeamount());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("代收金额：" + String.format("%.2f", mao.getIncomeamount()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 代收充值
	@Override
	public synchronized void updateIncome(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getMerchantname());
			scr.setType(DictionaryResource.ORDERTYPE_27);
			scr.setPretotalincome(t.getTotalincome());
			scr.setPosttotalincome(t.getTotalincome() + mao.getIncomeamount());
			scr.setAmount(mao.getIncomeamount());
			t.setTotalincome(t.getTotalincome() + mao.getIncomeamount());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("代收充值金额：" + String.format("%.2f", mao.getIncomeamount()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}