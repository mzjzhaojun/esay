package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.yt.app.api.v1.entity.Channelaccountrecord;
import com.yt.app.api.v1.entity.Channelaccountorder;
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
	 * =============================================================收入
	 * 
	 */

	@Transactional
	private void setToincomeamount(Channelaccount ma, Channelaccountrecord aaaj) {
		// 更新待收入
		ma.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(ma);

		channelaccountapplyjournamapper.post(aaaj);
	}

	@Transactional
	private void setTotalincome(Channelaccount t, Channelaccountrecord aaaj) {
		// 收入增加金额
		t.setTotalincome(aaaj.getPosttotalincome());
		// 待收入减去金额.
		t.setToincomeamount(aaaj.getPretoincomeamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Channel a = channelmapper.get(t.getChannelid());
		a.setBalance(t.getBalance());
		channelmapper.put(a);

		channelaccountapplyjournamapper.post(aaaj);
	}

	// 待确认收入
	@Override
	public  void totalincome(Channelaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByUserId(t.getUserid());
			// 资金记录
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("待确认收入￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			setToincomeamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	public  void updateTotalincome(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());

			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_31);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome() + mao.getAmountreceived());// 总收入
			aaaj.setPosttoincomeamount(mao.getAmountreceived());// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("充值收入￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setTotalincome(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝收入
	@Override
	public  void turndownTotalincome(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_32);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("失败收入￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setToincomeamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	public  void cancleTotalincome(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("取消收入￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setToincomeamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================支出
	 * 
	 */
	@Transactional
	private void setWithdrawamount(Channelaccount t, Channelaccountrecord aaaj) {
		// 支出总金额
		t.setWithdrawamount(aaaj.getPostwithdrawamount());
		// 待支出减去金额
		t.setTowithdrawamount(aaaj.getPretowithdrawamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		m.setCount(m.getCount() + aaaj.getPosttowithdrawamount());
		m.setTodaycount(m.getTodaycount() + aaaj.getPosttowithdrawamount());
		m.setBalance(t.getBalance());
		m.setTodayincomecount(m.getTodayincomecount() + aaaj.getPosttowithdrawamount());
		m.setIncomecount(m.getIncomecount() + aaaj.getPosttowithdrawamount());
		channelmapper.put(m);

		channelaccountapplyjournamapper.post(aaaj);
	}

	// 待确认支出
	@Override
	public  void withdrawamount(Channelaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByUserId(t.getUserid());
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_34);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmountreceived());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("待确认￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			setWithdrawamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	public  void updateWithdrawamount(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
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
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
			aaaj.setPosttowithdrawamount(mao.getAmountreceived());// 待确认支出
			aaaj.setRemark("成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒絕支出
	@Override
	public  void turndownWithdrawamount(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_36);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("拒绝￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	public  void cancleWithdrawamount(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
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
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("取消￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}