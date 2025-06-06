package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountrecordMapper;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.PayoutMerchantaccountrecord;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Payout;
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
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class PayoutMerchantaccountServiceImpl extends YtBaseServiceImpl<PayoutMerchantaccount, Long> implements PayoutMerchantaccountService {

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private PayoutMerchantaccountMapper mapper;

	@Autowired
	private MerchantaccountbankMapper merchantaccountbankmapper;

	@Autowired
	private PayoutMerchantaccountrecordMapper merchantaccountapplyjournalmapper;

	@Override
	@Transactional
	public Integer post(PayoutMerchantaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<PayoutMerchantaccount> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<PayoutMerchantaccount>(Collections.emptyList());
			}
		}
		List<PayoutMerchantaccount> list = mapper.list(param);
		return new YtPageBean<PayoutMerchantaccount>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayoutMerchantaccount get(Long id) {
		PayoutMerchantaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayoutMerchantaccount getData() {
		PayoutMerchantaccount t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayoutMerchantaccount getDataBank() {
		PayoutMerchantaccount t = mapper.getByUserId(SysUserContext.getUserId());
		List<Merchantaccountbank> listbanks = merchantaccountbankmapper.listByUserid(SysUserContext.getUserId());
		t.setListbanks(listbanks);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayoutMerchantaccount getDataBank(Long id) {
		Merchant m = merchantmapper.get(id);
		PayoutMerchantaccount t = mapper.getByUserId(m.getUserid());
		List<Merchantaccountbank> listbanks = merchantaccountbankmapper.listByUserid(m.getUserid());
		t.setListbanks(listbanks);
		return t;
	}

	/**
	 * ============================================================= 待确认充值
	 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setToincomeamount(PayoutMerchantaccount ma, Merchantaccountorder t) {
		// 更新待确认金额
		ma.setToincomeamount(ma.getToincomeamount() + t.getAmountreceived());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(PayoutMerchantaccount ma, Merchantaccountorder t) {
		// 更新待确认金额
		ma.setToincomeamount(ma.getToincomeamount() - t.getAmountreceived());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successTotalincome(PayoutMerchantaccount t, Merchantaccountorder pm) {
		// 收入增加金额
		t.setTotalincome(t.getTotalincome() + pm.getAmountreceived());
		// 待收入减去金额.
		t.setToincomeamount(t.getToincomeamount() - pm.getAmountreceived());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);
		// 更新账户余额
		Merchant a = merchantmapper.get(pm.getMerchantid());

		// 余额
		a.setUsdtbalance(t.getBalance());
//		// 成功订单总数
//		a.setSuccess(a.getSuccess() + 1);
//		// 总入款金额
//		a.setCount(a.getCount() + pm.getAmount());
//		// 当日入款金额
//		a.setTodaycount(a.getTodaycount() + pm.getAmount());
//		// 总入款扣点后
//		a.setIncomecount(a.getIncomecount() + pm.getAmount());
//		// 当日入款扣点后
//		a.setTodayincomecount(a.getTodayincomecount() + pm.getAmount());

		merchantmapper.put(a);
	}

	///////////////////////////////////////////////////////////////////////// 充值待确认//////////////////////////////////////
	@Override
	public void totalincome(Merchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getUsername());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmountreceived());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入金额
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出金额

			maaj.setRemark("收入待确认￥：" + String.format("%.2f", t.getAmountreceived()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			setToincomeamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ============================================================= 确认充值成功
	 */
	@Override
	public void updateTotalincome(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());

			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_31);
			//
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			//
			maaj.setPosttotalincome(t.getTotalincome() + mao.getAmountreceived());// 总收入
			maaj.setPosttoincomeamount(mao.getAmountreceived());// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("充值成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			successTotalincome(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	public void cancleTotalincome(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("收入取消￥：" + String.format("%.2f", mao.getAmountreceived()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ============================================================提现
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setPretowithdrawamount(PayoutMerchantaccount ma, Merchantaccountorder maaj) {
		// 待支出金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() + maaj.getAmountreceived());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setUsdtbalance(ma.getBalance());
		m.setCountorder(m.getCountorder() + 1);
		merchantmapper.put(m);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelPretowithdrawamount(PayoutMerchantaccount ma, Merchantaccountorder maaj) {
		// 待支出金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() - maaj.getAmountreceived());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setUsdtbalance(ma.getBalance());
		merchantmapper.put(m);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successWithdrawamount(PayoutMerchantaccount ma, Merchantaccountorder pm) {
		// 总支出金额更新
		ma.setWithdrawamount(ma.getWithdrawamount() + pm.getAmountreceived());
		// 待支出减掉
		ma.setTowithdrawamount(ma.getTowithdrawamount() - pm.getAmountreceived());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());

		// 余额
		m.setUsdtbalance(ma.getBalance());

//		// 总支出
//		m.setCount(m.getCount() + pm.getAmountreceived());
//		// 当日支出
//		m.setTodaycount(m.getTodaycount() + pm.getAmountreceived());
//		// 代付成功订单
//		m.setSuccess(m.getSuccess() + 1);
//		// 当日支出扣点
//		m.setTodayincomecount(m.getTodayincomecount() + pm.getAmountreceived());
//		// 总支出扣点
//		m.setIncomecount(m.getIncomecount() + pm.getAmountreceived());
//		// 手续费
//		m.setTodaycost(m.getTodaycost() + m.getOnecost());

		merchantmapper.put(m);
	}

	// 待确认支出
	@Override
	public void withdrawamount(Merchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getUsername());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_32);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("待提现￥：" + String.format("%.2f", t.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			setPretowithdrawamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现成功
	@Override
	public void updateWithdrawamount(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			//
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_36);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmount());// 总支出
			maaj.setPosttowithdrawamount(mao.getAmount());// 确认支出
			maaj.setRemark("提现成功￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			successWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消提现
	@Override
	public void cancleWithdrawamount(Merchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_38);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("取消提现￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelPretowithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ============================================================代付
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setPretowithdrawamount(PayoutMerchantaccount ma, Payout maaj) {
		// 待支出金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() + maaj.getAmount());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setUsdtbalance(ma.getBalance());
		m.setCountorder(m.getCountorder() + 1);
		merchantmapper.put(m);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelPretowithdrawamount(PayoutMerchantaccount ma, Payout maaj) {
		// 待支出金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() - maaj.getAmount());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setUsdtbalance(ma.getBalance());
		merchantmapper.put(m);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successWithdrawamount(PayoutMerchantaccount ma, Payout pm) {
		// 总支出金额更新
		ma.setWithdrawamount(ma.getWithdrawamount() + pm.getAmount());
		// 待支出减掉
		ma.setTowithdrawamount(ma.getTowithdrawamount() - pm.getAmount());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		// 总支出
		m.setCount(m.getCount() + pm.getAmount());
		// 当日支出
		m.setTodaycount(m.getTodaycount() + pm.getAmount());
		// 代付成功订单
		m.setSuccess(m.getSuccess() + 1);
		// 余额
		m.setUsdtbalance(ma.getBalance());
		// 当日支出扣点
		m.setTodayincomecount(m.getTodayincomecount() + pm.getAmount());
		// 总支出扣点
		m.setIncomecount(m.getIncomecount() + pm.getAmount());
		// 手续费
		m.setTodaycost(m.getTodaycost() + m.getOnecost());

		merchantmapper.put(m);
	}

	// 待确认支出
	@Override
	public void withdrawamount(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getMerchantname());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_34);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("新代付￥：" + String.format("%.2f", t.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			setPretowithdrawamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现成功
	@Override
	public void updateWithdrawamount(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			//
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_35);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmount());// 总支出
			maaj.setPosttowithdrawamount(mao.getAmount());// 确认支出
			maaj.setRemark("代付成功￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			successWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消提现
	@Override
	public void cancleWithdrawamount(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_37);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("取消代付￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelPretowithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}