package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountrecordMapper;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaccountrecord;
import com.yt.app.api.v1.vo.QrcodeaccountVO;
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
 * @version v1 @createdate2024-08-23 22:50:47
 */

@Service
public class QrcodeaccountServiceImpl extends YtBaseServiceImpl<Qrcodeaccount, Long> implements QrcodeaccountService {
	@Autowired
	private QrcodeaccountMapper mapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Autowired
	private QrcodeaccountrecordMapper qrcodeaccountrecordmapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcodeaccount get(Long id) {
		Qrcodeaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeaccountVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountVO>(Collections.emptyList());
		}
		List<QrcodeaccountVO> list = mapper.page(param);
		return new YtPageBean<QrcodeaccountVO>(param, list, count);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setToincomeamount(Qrcodeaccount ma, Qrcodeaccountorder mao) {
		// 更新帶收入金額
		ma.setToincomeamount(ma.getToincomeamount() + mao.getIncomeamount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(Qrcodeaccount ma, Qrcodeaccountorder mao) {
		// 更新帶收入金額
		ma.setToincomeamount(ma.getToincomeamount() - mao.getIncomeamount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void scuccessTotalincome(Qrcodeaccount t, Qrcodeaccountorder qo) {
		// 收入金额增加
		t.setTotalincome(t.getTotalincome() + qo.getIncomeamount());
		// 代收金额更新
		t.setToincomeamount(t.getToincomeamount() - qo.getIncomeamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		m.setCount(m.getCount() + qo.getIncomeamount());
		m.setTodaycount(m.getTodaycount() + qo.getIncomeamount());
		m.setBalance(t.getBalance());
		m.setTodayincomecount(m.getTodayincomecount() + qo.getAmount());
		m.setIncomecount(m.getIncomecount() + qo.getAmount());
		channelmapper.put(m);
	}

	/**
	 * 代收新增
	 */
	@Override
	public void totalincome(Qrcodeaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount ma = mapper.getByUserId(t.getUserid());
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();

			maaj.setUserid(t.getUserid());
			maaj.setChannelname(t.getQrcodename());
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

			maaj.setRemark("码商代收人民币￥：" + String.format("%.2f", t.getIncomeamount()));
			qrcodeaccountrecordmapper.post(maaj);
			//
			setToincomeamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 代收成功
	 */
	@Override
	public void updateTotalincome(Qrcodeaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount t = mapper.getByUserId(mao.getUserid());
			//
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setChannelname(mao.getQrcodename());
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
			maaj.setRemark("码商代收成功￥：" + String.format("%.2f", mao.getIncomeamount()));
			qrcodeaccountrecordmapper.post(maaj);
			//
			scuccessTotalincome(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 超时取消
	@Override
	public void cancleTotalincome(Qrcodeaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount t = mapper.getByUserId(mao.getUserid());

			//
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setChannelname(mao.getQrcodename());
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
			qrcodeaccountrecordmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Qrcodeaccount getData() {
		Qrcodeaccount t = mapper.getByUserId(SysUserContext.getUserId());
		return t;
	}
}