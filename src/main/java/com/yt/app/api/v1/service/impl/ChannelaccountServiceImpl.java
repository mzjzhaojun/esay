package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ChannelaccountMapper;
import com.yt.app.api.v1.mapper.ChannelaccountrecordMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
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
public class ChannelaccountServiceImpl extends YtBaseServiceImpl<Channelaccount, Long>
		implements ChannelaccountService {
	@Autowired
	private ChannelaccountMapper mapper;
	@Autowired
	private ChannelService channelservice;
	@Autowired
	private ChannelaccountrecordMapper channelaccountapplyjournamapper;

	@Override
	@Transactional
	public Integer post(Channelaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Channelaccount> list(Map<String, Object> param) {
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

	// 待确认收入
	@Override
	@Transactional
	public void totalincome(Channelaccountorder t) {
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
			aaaj.setRemark("待确认充值￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);

			ma.setToincomeamount(aaaj.getPretoincomeamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	@Transactional
	public void updateTotalincome(Channelaccountorder mao) {
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
			aaaj.setRemark("充值成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);

			t.setTotalincome(aaaj.getPosttotalincome());// 收入增加金额
			t.setToincomeamount(aaaj.getPretoincomeamount());// 待收入减去金额.
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);

			channelservice.updateIncome(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝收入
	@Override
	@Transactional
	public void turndownTotalincome(Channelaccountorder mao) {
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
			aaaj.setRemark("审核拒绝充值￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			t.setToincomeamount(aaaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	@Transactional
	public void cancleTotalincome(Channelaccountorder mao) {
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
			aaaj.setRemark("取消充值￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);

			t.setToincomeamount(aaaj.getPretoincomeamount());
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

	// 待确认支出
	@Override
	@Transactional
	public void withdrawamount(Channelaccountorder t) {
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
			aaaj.setRemark("代付待确认￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			ma.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	@Transactional
	public void updateWithdrawamount(Channelaccountorder mao) {
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
			aaaj.setRemark("代付成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			t.setWithdrawamount(aaaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒絕支出
	@Override
	@Transactional
	public void turndownWithdrawamount(Channelaccountorder mao) {
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
			aaaj.setRemark("代付失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);

			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	@Transactional
	public void cancleWithdrawamount(Channelaccountorder mao) {
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
			aaaj.setRemark("取消代付￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
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

	// 待确认支出
	@Override
	@Transactional
	public void exchangeamount(Channelaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Channelaccount ma = mapper.getByUserId(t.getUserid());
			Channelaccountrecord aaaj = new Channelaccountrecord();

			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(t.getChannelname());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_94);
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
			aaaj.setRemark("待确认换汇￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			ma.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	@Transactional
	public void updateexchangeamount(Channelaccountorder mao) {
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
			aaaj.setType(DictionaryResource.RECORDTYPE_95);

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
			aaaj.setRemark("换汇成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			t.setWithdrawamount(aaaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒絕支出
	@Override
	@Transactional
	public void turndownexchangeamount(Channelaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Channelaccount t = mapper.getByUserId(mao.getUserid());
			//
			Channelaccountrecord aaaj = new Channelaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getChannelname());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_96);

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
			aaaj.setRemark("换汇失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);

			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	@Transactional
	public void cancleexchangeamount(Channelaccountorder mao) {
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
			aaaj.setType(DictionaryResource.RECORDTYPE_97);

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
			aaaj.setRemark("取消换汇￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			channelaccountapplyjournamapper.post(aaaj);
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}