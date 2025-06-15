package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountMapper;
import com.yt.app.api.v1.mapper.ChannelaccountrecordMapper;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Channelaccountrecord;
import com.yt.app.api.v1.entity.Income;
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
public class ChannelaccountServiceImpl extends YtBaseServiceImpl<Channelaccount, Long> implements ChannelaccountService {
	@Autowired
	private ChannelaccountMapper mapper;
	@Autowired
	private ChannelaccountrecordMapper channelaccountapplyjournamapper;
	@Autowired
	private ChannelMapper channelmapper;

	@Override
	@Transactional
	public Integer post(Channelaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Channelaccount> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Channelaccount>(Collections.emptyList());
			}
		}
		List<Channelaccount> list = mapper.list(param);
		return new YtPageBean<Channelaccount>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Channelaccount get(Long id) {
		Channelaccount t = mapper.get(id);
		return t;
	}

	/**
	 * =============================================================充值
	 * 
	 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setToincomeamount(Channelaccount t, Channelaccountrecord aaaj) {
		// 更新待收入
		t.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(t);

		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setTotalincome(Channelaccountorder in, Channelaccount t, Channelaccountrecord aaaj) {
		// 收入增加金额
		t.setTotalincome(aaaj.getPosttotalincome());
		// 待收入减去金额.
		t.setToincomeamount(aaaj.getPretoincomeamount());
		//
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		//
		mapper.put(t);

		Channel a = channelmapper.get(t.getChannelid());
		// 余额
		a.setBalance(t.getBalance());
		// 更新
		channelmapper.put(a);
		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(Channelaccount t, Channelaccountrecord aaaj) {
		// 更新待收入
		t.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(t);
		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	// 待确认收入
	@Override
	public void totalincome(Channelaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByChannelId(t.getChannelid());
			// 资金记录
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(ma.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("待确认充值￥：" + String.format("%.2f", t.getAmount()));
			//
			setToincomeamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	public void updateTotalincome(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());

			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_31);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome() + mao.getAmount());// 总收入
			aaaj.setPosttoincomeamount(mao.getAmount());// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("充值成功￥：" + String.format("%.2f", mao.getAmount()));
			//
			setTotalincome(mao, t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消
	@Override
	public void cancleTotalincome(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("充值取消￥：" + String.format("%.2f", mao.getAmount()));
			//
			cancelToincomeamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================代收
	 * 
	 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setToincomeamount(Income in, Channelaccount t, Channelaccountrecord aaaj) {
		// 更新待收入
		t.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(t);
		Channel m = channelmapper.get(t.getChannelid());
		// 余额
		m.setCountorder(m.getCountorder() + 1);
		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setTotalincome(Income in, Channelaccount t, Channelaccountrecord aaaj) {
		// 收入增加金额
		t.setTotalincome(aaaj.getPosttotalincome());
		// 待收入减去金额.
		t.setToincomeamount(aaaj.getPretoincomeamount());
		//
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		//
		mapper.put(t);

		Channel a = channelmapper.get(t.getChannelid());
		// 余额
		a.setBalance(t.getBalance());
		// 成功订单总数
		a.setSuccess(a.getSuccess() + 1);
		// 总入款金额
		a.setCount(a.getCount() + in.getAmount());
		// 当日入款金额
		a.setTodaycount(a.getTodaycount() + in.getAmount());
		// 总入款扣点后
		a.setIncomecount(a.getIncomecount() + in.getChannelincomeamount());
		// 当日入款扣点后
		a.setTodayincomecount(a.getTodayincomecount() + in.getChannelincomeamount());
		// 更新
		channelmapper.put(a);
		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(Income mao, Channelaccount t, Channelaccountrecord aaaj) {
		// 更新待收入
		t.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(t);
		// 操作记录
		channelaccountapplyjournamapper.post(aaaj);
	}

	// 待确认收入
	@Override
	public void totalincome(Income t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByChannelId(t.getChannelid());
			// 资金记录
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(ma.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_39);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("待确认代收￥：" + String.format("%.2f", t.getAmount()));
			//
			setToincomeamount(t, ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	public void updateTotalincome(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());

			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_46);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome() + mao.getAmount());// 总收入
			aaaj.setPosttoincomeamount(mao.getAmount());// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("代收成功￥：" + String.format("%.2f", mao.getAmount()));
			//
			setTotalincome(mao, t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消
	@Override
	public void cancleTotalincome(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_47);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("代收取消￥：" + String.format("%.2f", mao.getAmount()));
			//
			cancelToincomeamount(mao, t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================代付
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setWithdrawamount(Channelaccount t, Channelaccountrecord aaaj) {
		// 支出总金额
		t.setWithdrawamount(aaaj.getPostwithdrawamount());
		// 待支出减去金额
		t.setTowithdrawamount(aaaj.getPretowithdrawamount());
		// 余额
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		//
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		// 余额
		m.setBalance(t.getBalance());
		m.setCountorder(m.getCountorder() + 1);
		//
		channelmapper.put(m);

		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setWithdrawt(Payout mao, Channelaccount t, Channelaccountrecord aaaj) {
		// 支出总金额
		t.setWithdrawamount(aaaj.getPostwithdrawamount());
		// 待支出减去金额
		t.setTowithdrawamount(aaaj.getPretowithdrawamount());
		// 余额
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		//
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		// 成功订单总数
		m.setSuccess(m.getSuccess() + 1);
		// 总支出
		m.setCount(m.getCount() + mao.getChannelpay());
		// 当日支出
		m.setTodaycount(m.getTodaycount() + mao.getChannelpay());
		// 余额
		m.setBalance(t.getBalance());
		// 当日支出扣点
		m.setTodayincomecount(m.getTodayincomecount() + mao.getChanneldeal());
		// 总支出扣点
		m.setIncomecount(m.getIncomecount() + mao.getChanneldeal());
		// 手续费
		m.setTodaycost(m.getTodaycost() + m.getOnecost());

		channelmapper.put(m);

		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelWithdrawamount(Channelaccount t, Channelaccountrecord aaaj) {
		// 支出总金额
		t.setWithdrawamount(aaaj.getPostwithdrawamount());
		// 待支出减去金额
		t.setTowithdrawamount(aaaj.getPretowithdrawamount());
		// 余额
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		//
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		// 余额
		m.setBalance(t.getBalance());
		//
		channelmapper.put(m);

		channelaccountapplyjournamapper.post(aaaj);
	}

	// 待确认支出
	@Override
	public void withdrawamount(Payout t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByChannelId(t.getChannelid());
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(ma.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_34);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getChannelpay());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("待确认支出￥：" + String.format("%.2f", t.getChannelpay()));
			//
			setWithdrawamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	public void updateWithdrawamount(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			//
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_35);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getChannelpay());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getChannelpay());// 总支出
			aaaj.setPosttowithdrawamount(mao.getChannelpay());// 待确认支出
			aaaj.setRemark("支出成功￥：" + String.format("%.2f", mao.getChannelpay()));
			setWithdrawt(mao, t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	public void cancleWithdrawamount(Payout mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByChannelId(mao.getChannelid());
			//
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_37);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getChannelpay());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("支出取消￥：" + String.format("%.2f", mao.getChannelpay()));
			//
			cancelWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}