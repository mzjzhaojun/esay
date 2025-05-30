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
	private void setToincomeamount(PayoutMerchantaccount ma, Payout t) {
		// 更新待确认金额
		ma.setToincomeamount(ma.getToincomeamount() + t.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(PayoutMerchantaccount ma, Payout t) {
		// 更新待确认金额
		ma.setToincomeamount(ma.getToincomeamount() - t.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successTotalincome(PayoutMerchantaccount t, Payout pm) {
		// 收入增加金额
		t.setTotalincome(t.getTotalincome() + pm.getAmount());
		// 待收入减去金额.
		t.setToincomeamount(t.getToincomeamount() - pm.getAmount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);
		// 更新余额
		Merchant m = merchantmapper.get(t.getMerchantid());
		m.setUsdtbalance(t.getBalance());
		merchantmapper.put(m);
	}

	@Override
	public void totalincome(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getMerchantname());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入金额
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出金额

			maaj.setRemark("收入待确认￥：" + String.format("%.2f", t.getAmount()));
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
	public void updateTotalincome(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());

			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_31);
			//
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			//
			maaj.setPosttotalincome(t.getTotalincome() + mao.getAmount());// 总收入
			maaj.setPosttoincomeamount(mao.getAmount());// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("充值成功￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			successTotalincome(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝充值
	@Override
	public void turndownTotalincome(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_32);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("收入拒绝￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	public void cancleTotalincome(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("收入取消￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ============================================================支出
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
	private void successWithdrawamount(PayoutMerchantaccount ma, Payout maaj) {
		// 总支出金额更新
		ma.setWithdrawamount(ma.getWithdrawamount() + maaj.getAmount());
		// 待支出减掉
		ma.setTowithdrawamount(ma.getTowithdrawamount() - maaj.getAmount());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setCount(m.getCount() + maaj.getAmount());
		m.setTodaycount(m.getTodaycount() + maaj.getAmount());
		m.setUsdtbalance(ma.getBalance());
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
			maaj.setRemark("待支出￥：" + String.format("%.2f", t.getAmount()));
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
			maaj.setRemark("支出成功￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			successWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现失败
	@Override
	public void turndownWithdrawamount(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
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
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("拒绝支出￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelPretowithdrawamount(t, mao);
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
			maaj.setRemark("取消支出￥：" + String.format("%.2f", mao.getAmount()));
			merchantaccountapplyjournalmapper.post(maaj);
			//
			cancelPretowithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}