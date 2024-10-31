package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomemerchantaccountMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountrecordMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountrecordMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Incomemerchantaccountrecord;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.PayoutMerchantaccountrecord;
import com.yt.app.api.v1.vo.IncomemerchantaccountVO;
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
 * @version v1 @createdate2024-08-22 23:02:54
 */

@Service
public class IncomemerchantaccountServiceImpl extends YtBaseServiceImpl<Incomemerchantaccount, Long> implements IncomemerchantaccountService {
	@Autowired
	private IncomemerchantaccountMapper mapper;

	@Autowired
	private IncomemerchantaccountrecordMapper incomemerchantaccountrecordmapper;

	@Autowired
	private PayoutMerchantaccountrecordMapper payoutmerchantaccountrecordmapper;

	@Autowired
	private MerchantService merchantservice;

	@Override
	@Transactional
	public Integer post(Incomemerchantaccount t) {
		Integer i = mapper.post(t);
		return i;
	}


	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Incomemerchantaccount get(Long id) {
		Incomemerchantaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<IncomemerchantaccountVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomemerchantaccountVO>(Collections.emptyList());
		}
		List<IncomemerchantaccountVO> list = mapper.page(param);
		return new YtPageBean<IncomemerchantaccountVO>(param, list, count);
	}

	/**
	 * 新增代收
	 */
	@Override
	@Transactional
	public synchronized void totalincome(Incomemerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		lock.lock();
		try {
			Incomemerchantaccount ma = mapper.getByUserId(t.getUserid());
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(t.getMerchantname());
			maaj.setOrdernum(t.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			maaj.setPretotalincome(ma.getTotalincome());// 总收入
			maaj.setPretoincomeamount(ma.getToincomeamount() + t.getIncomeamount());// 待确认收入
			maaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(ma.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入金额
			maaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出金额

			maaj.setRemark("商户新增代收人民币￥：" + String.format("%.2f", t.getIncomeamount()));
			//
			incomemerchantaccountrecordmapper.post(maaj);
			ma.setToincomeamount(maaj.getPretoincomeamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 代收 成功
	 */
	@Override
	@Transactional
	public synchronized void updateTotalincome(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		lock.lock();
		try {
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());

			//
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_31);
			//
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getIncomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			//
			maaj.setPosttotalincome(t.getTotalincome() + mao.getIncomeamount());// 总收入
			maaj.setPosttoincomeamount(mao.getIncomeamount());// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("商户代收成功￥：" + String.format("%.2f", mao.getIncomeamount()));
			//
			incomemerchantaccountrecordmapper.post(maaj);

			t.setTotalincome(maaj.getPosttotalincome());// 收入增加金额
			t.setToincomeamount(maaj.getPretoincomeamount());// 待收入减去金额.
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 超时取消
	@Override
	@Transactional
	public synchronized void cancleTotalincome(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		lock.lock();
		try {
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());

			//
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setMerchantname(mao.getMerchantname());
			maaj.setOrdernum(mao.getOrdernum());
			maaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			maaj.setPretotalincome(t.getTotalincome());// 总收入
			maaj.setPretoincomeamount(t.getToincomeamount() - mao.getIncomeamount());// 待确认收入
			maaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			maaj.setPosttotalincome(t.getTotalincome());// 总收入
			maaj.setPosttoincomeamount(0.00);// 确认收入
			maaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			maaj.setPosttowithdrawamount(0.00);// 确认支出
			maaj.setRemark("超时支付,取消订单：" + String.format("%.2f", mao.getIncomeamount()));
			//
			incomemerchantaccountrecordmapper.post(maaj);

			t.setToincomeamount(maaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Incomemerchantaccount getData() {
		Incomemerchantaccount t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Incomemerchantaccount getDataBank(Long id) {
		Incomemerchantaccount t = mapper.getByMerchantId(id);
		return t;
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
		lock.lock();
		try {
			Incomemerchantaccount ma = mapper.getByUserId(t.getUserid());
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
			maaj.setRemark("冻结待提现￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			payoutmerchantaccountrecordmapper.post(maaj);

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
		lock.lock();
		try {
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
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
			maaj.setRemark("提现成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			payoutmerchantaccountrecordmapper.post(maaj);

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
		lock.lock();
		try {
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
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
			maaj.setRemark("审核拒绝提现￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			payoutmerchantaccountrecordmapper.post(maaj);

			t.setTowithdrawamount(maaj.getPretowithdrawamount());
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
		public synchronized void cancelWithdrawamount(PayoutMerchantaccountorder mao) {
			RLock lock = RedissonUtil.getLock(mao.getMerchantid());
			lock.lock();
			try {
				Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
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
				maaj.setRemark("取消提现￥：" + String.format("%.2f", mao.getAmountreceived()));
				//
				payoutmerchantaccountrecordmapper.post(maaj);

				t.setTowithdrawamount(maaj.getPretowithdrawamount());
				t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
				mapper.put(t);
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
		}
}