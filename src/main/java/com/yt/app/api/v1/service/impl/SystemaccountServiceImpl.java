package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
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
import com.yt.app.api.v1.entity.Payout;
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updatePayout(Systemaccount t, Payout mao) {
		t.setPototalincome(t.getPototalincome() + mao.getIncome());
		t.setPobalance(t.getPototalincome() - t.getPowithdrawamount());
		t.setTodaypayout(t.getTodaypayout() + mao.getIncome());
		mapper.put(t);
	}

	/**
	 * 商户代付成功收入
	 */
	@Override
	public void updatePayout(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getMerchantname());
			scr.setType(DictionaryResource.ORDERTYPE_23);
			scr.setPoprewithdrawamount(t.getPowithdrawamount());
			scr.setPopostwithdrawamount(t.getPowithdrawamount() + mao.getIncome());
			scr.setPoamount(mao.getIncome());
			scr.setPobalance(t.getPobalance());//
			scr.setAmount(mao.getAmount());
			scr.setRemark("代付金额：" + String.format("%.2f", mao.getAmount()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);

			updatePayout(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateBalance(Systemaccount t, Income mao) {
		t.setTotalincome(t.getTotalincome() + mao.getIncomeamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount());
		t.setTodayincome(t.getTodayincome() + mao.getIncomeamount());
		mapper.put(t);
	}

	// 代收成功收款
	@Override
	public void updateIncome(Income mao) {
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
			scr.setBalance(t.getBalance());//
			scr.setRemark("代收金额：" + String.format("%.2f", mao.getIncomeamount()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);

			updateBalance(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateBalance(Systemaccount t, Incomemerchantaccountorder mao) {
		t.setTotalincome(t.getTotalincome() + mao.getIncomeamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount());
		mapper.put(t);
	}

	@Override
	public void updateAgentWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemaccountrecord scr = new Systemaccountrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_26);
			scr.setPoprewithdrawamount(t.getWithdrawamount());
			scr.setPopostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setPoamount(mao.getAmountreceived());
			scr.setPobalance(t.getBalance());//
			scr.setRemark("代理提现金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);

			updateAgentwithdraw(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateAgentwithdraw(Systemaccount t, Agentaccountorder mao) {
		t.setPowithdrawamount(t.getPowithdrawamount() + mao.getAmountreceived());
		t.setPobalance(t.getPototalincome() - t.getPowithdrawamount());
		mapper.put(t);
	}

}