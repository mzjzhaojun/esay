package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.ExchangeMerchantaccountMapper;
import com.yt.app.api.v1.mapper.ExchangeMerchantaccountrecordMapper;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.ExchangeMerchantaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountrecord;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
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
public class ExchangeMerchantaccountServiceImpl extends YtBaseServiceImpl<ExchangeMerchantaccount, Long>
		implements ExchangeMerchantaccountService {

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private MerchantService merchantservice;

	@Autowired
	private ExchangeMerchantaccountMapper mapper;

	@Autowired
	private MerchantaccountbankMapper merchantaccountbankmapper;

	@Autowired
	private ExchangeMerchantaccountrecordMapper exchangemerchantaccountapplyjournalmapper;

	@Override
	@Transactional
	public Integer post(ExchangeMerchantaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<ExchangeMerchantaccount> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<ExchangeMerchantaccount>(Collections.emptyList());
			}
		}
		List<ExchangeMerchantaccount> list = mapper.list(param);
		return new YtPageBean<ExchangeMerchantaccount>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ExchangeMerchantaccount get(Long id) {
		ExchangeMerchantaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ExchangeMerchantaccount getData() {
		ExchangeMerchantaccount t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ExchangeMerchantaccount getDataBank() {
		ExchangeMerchantaccount t = mapper.getByUserId(SysUserContext.getUserId());
		List<Merchantaccountbank> listbanks = merchantaccountbankmapper.listByUserid(SysUserContext.getUserId());
		t.setListbanks(listbanks);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ExchangeMerchantaccount getDataBank(Long id) {
		Merchant m = merchantmapper.get(id);
		ExchangeMerchantaccount t = mapper.getByUserId(m.getUserid());
		List<Merchantaccountbank> listbanks = merchantaccountbankmapper.listByUserid(m.getUserid());
		t.setListbanks(listbanks);
		return t;
	}

	/**
	 * ============================================================= 待确认充值
	 */
	@Override
	@Transactional
	public void totalincome(ExchangeMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount ma = mapper.getByUserId(t.getUserid());
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();

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

			maaj.setRemark("充值USDT待确认：" + String.format("%.2f", t.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void updateTotalincome(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());

			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("充值USDT成功：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);

			t.setTotalincome(maaj.getPosttotalincome());// 收入增加金额
			t.setToincomeamount(maaj.getPretoincomeamount());// 待收入减去金额.
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
			// 更新余额
			merchantservice.updateInComeUsdt(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝充值
	@Override
	@Transactional
	public void turndownTotalincome(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("审核拒绝USDT充值：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);

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
	public void cancleTotalincome(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("取消USDT充值：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);

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
	public void withdrawamount(ExchangeMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount ma = mapper.getByUserId(t.getUserid());
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();

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
			maaj.setRemark("冻结USDT待提现：" + String.format("%.2f", t.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);

			ma.setWithdrawamount(maaj.getPostwithdrawamount());// 支出增加金额
			ma.setTowithdrawamount(maaj.getPretowithdrawamount());// 待支出减去金额
			ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
			mapper.put(ma);

			merchantservice.withdrawamountUsdt(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现成功
	@Override
	@Transactional
	public void updateWithdrawamount(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("提现USDT成功：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void turndownWithdrawamount(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("审核拒绝USDT提现：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void cancleWithdrawamount(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			maaj.setRemark("取消USDT提现：" + String.format("%.2f", mao.getAmountreceived()));
			//
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void payout(ExchangeMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount ma = mapper.getByUserId(t.getUserid());
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();

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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void updatePayout(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void turndownPayout(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void failPayout(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.postAndTenantid(maaj);
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
	public void canclePayout(ExchangeMerchantaccountorder mao) {
		//
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void exchange(ExchangeMerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount ma = mapper.getByUserId(t.getUserid());
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();

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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void updateExchange(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void turndownExchange(ExchangeMerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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
	public void cancleExchange(ExchangeMerchantaccountorder mao) {
		//
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			ExchangeMerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			//
			ExchangeMerchantaccountrecord maaj = new ExchangeMerchantaccountrecord();
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
			exchangemerchantaccountapplyjournalmapper.post(maaj);
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