package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.mapper.SystemcapitalrecordMapper;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.entity.Systemcapitalrecord;
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
	private SystemcapitalrecordMapper systemcapitalrecordmapper;

	@Override
	@Transactional
	public Integer post(Systemaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Systemaccount> list(Map<String, Object> param) {
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

	public Systemaccount getData() {
		Systemaccount t = mapper.get(JwtUserContext.get().getSystemaccountId());
		return t;
	}

	// 收入
	@Override
	@Transactional
	public void updateTotalincome(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemcapitalrecord scr = new Systemcapitalrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_20);
			scr.setPretotalincome(t.getTotalincome());// 总收入
			scr.setPosttotalincome(t.getTotalincome() + mao.getAmountreceived());// 待确认收入
			scr.setAmount(mao.getAmountreceived());// 操作金额
			t.setTotalincome(t.getTotalincome() + mao.getAmountreceived());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("商户充值金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 商户提现
	@Override
	@Transactional
	public void updateWithdrawamount(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemcapitalrecord scr = new Systemcapitalrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_21);
			scr.setPrewithdrawamount(t.getWithdrawamount());
			scr.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setAmount(mao.getAmountreceived());
			t.setWithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("商户提现金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 商户代付
	@Override
	@Transactional
	public void updatePayout(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemcapitalrecord scr = new Systemcapitalrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_23);
			scr.setPrewithdrawamount(t.getWithdrawamount());
			scr.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setAmount(mao.getAmountreceived());
			t.setWithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("商戶代付金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 商户换汇
	@Override
	@Transactional
	public void updateExchange(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemcapitalrecord scr = new Systemcapitalrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_22);
			scr.setPrewithdrawamount(t.getWithdrawamount());
			scr.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setAmount(mao.getAmountreceived());
			t.setWithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("商戶换汇金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 代理提现支出
	@Override
	@Transactional
	public void updateWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getTenant_id());
		try {
			lock.lock();
			Systemaccount t = mapper.getByTenantId(mao.getTenant_id());
			Systemcapitalrecord scr = new Systemcapitalrecord();
			scr.setSystemaccountid(t.getId());
			scr.setName(mao.getUsername());
			scr.setType(DictionaryResource.ORDERTYPE_21);
			scr.setPrewithdrawamount(t.getWithdrawamount());
			scr.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			scr.setAmount(mao.getAmountreceived());
			t.setWithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount());
			mapper.put(t);
			scr.setBalance(t.getBalance());//
			scr.setRemark("代理提现金额：" + String.format("%.2f", mao.getAmountreceived()) + "  单号:" + mao.getOrdernum());
			systemcapitalrecordmapper.post(scr);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}