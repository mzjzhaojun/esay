package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountrecordMapper;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.PayoutMerchantaccountrecord;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
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
	private MerchantService merchantservice;

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
	@Override
	@Transactional
	public synchronized void totalincome(PayoutMerchantaccountorder t) {
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

			maaj.setRemark("充值人民币待确认￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			ma.setToincomeamount(maaj.getPretoincomeamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * ============================================================= 确认充值成功
	 */
	@Override
	@Transactional
	public synchronized void updateTotalincome(PayoutMerchantaccountorder mao) {
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
			//
			merchantaccountapplyjournalmapper.post(maaj);

			t.setTotalincome(maaj.getPosttotalincome());// 收入增加金额
			t.setToincomeamount(maaj.getPretoincomeamount());// 待收入减去金额.
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
			// 更新余额
			merchantservice.updateInCome(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝充值
	@Override
	@Transactional
	public synchronized void turndownTotalincome(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_32);

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
			maaj.setRemark("审核拒绝充值￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);

			t.setToincomeamount(maaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	@Transactional
	public synchronized void cancleTotalincome(PayoutMerchantaccountorder mao) {
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
			maaj.setRemark("取消充值￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);

			t.setToincomeamount(maaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================提现
	 * 
	 */

	// 待确认支出
	@Override
	@Transactional
	public synchronized void withdrawamount(PayoutMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getUsername());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_90);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("冻结待提现￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);

			ma.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			ma.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
			mapper.put(ma);

			merchantservice.withdrawamount(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现成功
	@Override
	@Transactional
	public synchronized void updateWithdrawamount(PayoutMerchantaccountorder mao) {
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
			maaj.setType(DictionaryResource.RECORDTYPE_91);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
			maaj.setPosttowithdrawamount(mao.getAmountreceived());// 确认支出
			maaj.setRemark("提现成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现失败
	@Override
	@Transactional
	public synchronized void turndownWithdrawamount(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_92);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("审核拒绝提现￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消提现
	@Override
	@Transactional
	public synchronized void cancleWithdrawamount(PayoutMerchantaccountorder mao) {
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
			maaj.setType(DictionaryResource.RECORDTYPE_93);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("取消提现￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================代付
	 * 
	 */
	// 待确认代付
	@Override
	@Transactional
	public synchronized void payout(PayoutMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getUsername());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_34);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("待确认代付￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			ma.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出金额
			ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认代付
	@Override
	@Transactional
	public synchronized void updatePayout(PayoutMerchantaccountorder mao) {
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
			maaj.setType(DictionaryResource.RECORDTYPE_35);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
			maaj.setPosttowithdrawamount(mao.getAmountreceived());// 确认支出
			maaj.setRemark("代付成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			//
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝代付
	@Override
	@Transactional
	public synchronized void turndownPayout(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_36);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("代付失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// fail
	@Override
	@Transactional
	public synchronized void failPayout(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setTenant_id(t.getTenant_id());
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_36);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("代付失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.postAndTenantid(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消代付
	@Override
	@Transactional
	public synchronized void canclePayout(PayoutMerchantaccountorder mao) {
		//
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
			maaj.setType(DictionaryResource.RECORDTYPE_37);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("取消代付￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================换汇
	 * 
	 */
	// 待确认代付
	@Override
	@Transactional
	public synchronized void exchange(PayoutMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount ma = mapper.getByUserId(t.getUserid());
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getUsername());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_94);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("换汇待确认￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			ma.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出金额
			ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认代付
	@Override
	@Transactional
	public synchronized void updateExchange(PayoutMerchantaccountorder mao) {
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
			maaj.setType(DictionaryResource.RECORDTYPE_95);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
			maaj.setPosttowithdrawamount(mao.getAmountreceived());// 确认支出
			maaj.setRemark("换汇成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			//
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝代付
	@Override
	@Transactional
	public synchronized void turndownExchange(PayoutMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			PayoutMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			PayoutMerchantaccountrecord maaj = new PayoutMerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getUsername());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_96);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("换汇失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消代付
	@Override
	@Transactional
	public synchronized void cancleExchange(PayoutMerchantaccountorder mao) {
		//
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
			maaj.setType(DictionaryResource.RECORDTYPE_97);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("取消换汇￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			merchantaccountapplyjournalmapper.post(maaj);
			t.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			t.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}