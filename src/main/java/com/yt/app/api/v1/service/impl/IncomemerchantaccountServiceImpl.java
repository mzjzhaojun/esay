package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomemerchantaccountMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountrecordMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Incomemerchantaccountrecord;
import com.yt.app.api.v1.entity.Merchant;
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
	private MerchantMapper merchantmapper;

	@Autowired
	private IncomemerchantaccountrecordMapper incomemerchantaccountrecordmapper;

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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setToincomeamount(Incomemerchantaccount ma, Incomemerchantaccountorder mao) {
		// 更新待收入金额
		ma.setToincomeamount(ma.getToincomeamount() + mao.getIncomeamount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(Incomemerchantaccount ma, Incomemerchantaccountorder mao) {
		// 更新待收入金额
		ma.setToincomeamount(ma.getToincomeamount() - mao.getIncomeamount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setTotalincome(Incomemerchantaccount t, Incomemerchantaccountrecord maaj, Incomemerchantaccountorder mao) {
		// 收入增加金额
		t.setTotalincome(t.getTotalincome() + mao.getIncomeamount());
		// 待收入减去金额.
		t.setToincomeamount(t.getToincomeamount() - mao.getIncomeamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Merchant m = merchantmapper.get(t.getMerchantid());
		m.setCount(m.getCount() + mao.getIncomeamount());
		m.setTodaycount(m.getTodaycount() + mao.getIncomeamount());
		m.setBalance(t.getBalance());
		merchantmapper.put(m);
	}

	/**
	 * 新增代收
	 */
	@Override
	public void totalincome(Incomemerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
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

			maaj.setRemark("新增代收￥：" + String.format("%.2f", t.getIncomeamount()));

			incomemerchantaccountrecordmapper.post(maaj);
			//
			setToincomeamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 代收 成功
	 */
	@Override
	public void updateTotalincome(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
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
			maaj.setRemark("代收成功￥：" + String.format("%.2f", mao.getIncomeamount()));
			incomemerchantaccountrecordmapper.post(maaj);
			//
			setTotalincome(t, maaj, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 超时取消
	@Override
	public void cancleTotalincome(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
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
			maaj.setRemark("取消订单：" + String.format("%.2f", mao.getIncomeamount()));
			incomemerchantaccountrecordmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
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
	 * =============================================================支出
	 * 
	 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setWithdrawamount(Incomemerchantaccount ma, Incomemerchantaccountorder mao) {
		// 待支出加金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() + mao.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelWithdrawamount(Incomemerchantaccount ma, Incomemerchantaccountorder mao) {
		// 待支出减去金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() - mao.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successWithdrawamount(Incomemerchantaccount ma, Incomemerchantaccountorder mao) {
		// 待支出减去金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() - mao.getAmount());
		// 支出总金额更新
		ma.setWithdrawamount(ma.getWithdrawamount() + mao.getAmount());
		ma.setBalance(ma.getTotalincome() - ma.getWithdrawamount() - ma.getTowithdrawamount());
		mapper.put(ma);

		// 更新账户余额
		Merchant m = merchantmapper.get(ma.getMerchantid());
		m.setBalance(ma.getBalance());
		merchantmapper.put(m);
	}

	// 待确认支出
	@Override
	public void withdrawamount(Incomemerchantaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getMerchantid());
		try {
			lock.lock();
			Incomemerchantaccount ma = mapper.getByUserId(t.getUserid());
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();

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
			maaj.setRemark("新增支出￥：" + String.format("%.2f", t.getAmount()));
			incomemerchantaccountrecordmapper.post(maaj);
			//
			setWithdrawamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现成功
	@Override
	public void updateWithdrawamount(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();
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
			incomemerchantaccountrecordmapper.post(maaj);
			//
			successWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现失败
	@Override
	public void turndownWithdrawamount(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();
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
			incomemerchantaccountrecordmapper.post(maaj);
			//
			cancelWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 提现失败
	@Override
	public void cancelWithdrawamount(Incomemerchantaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getMerchantid());
		try {
			lock.lock();
			Incomemerchantaccount t = mapper.getByUserId(mao.getUserid());
			//
			Incomemerchantaccountrecord maaj = new Incomemerchantaccountrecord();
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
			incomemerchantaccountrecordmapper.post(maaj);
			//
			cancelWithdrawamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}