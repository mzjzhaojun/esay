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
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Qrcodeaccount;
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
	private void setToincomeamount(Qrcodeaccount ma, Income mao) {
		// 更新帶收入金額
		ma.setToincomeamount(ma.getToincomeamount() + mao.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void cancelToincomeamount(Qrcodeaccount ma, Income mao) {
		// 更新帶收入金額
		ma.setToincomeamount(ma.getToincomeamount() - mao.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void scuccessTotalincome(Qrcodeaccount t, Income qo) {
		// 收入金额增加
		t.setTotalincome(t.getTotalincome() + qo.getAmount());
		// 代收金额更新
		t.setToincomeamount(t.getToincomeamount() - qo.getAmount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		m.setCount(m.getCount() + qo.getIncomeamount());// 利润总和
		m.setTodaycount(m.getTodaycount() + qo.getIncomeamount());// 当日利润
		m.setBalance(t.getBalance());
		m.setTodayincomecount(m.getTodayincomecount() + qo.getAmount());// 当日支付
		m.setIncomecount(m.getIncomecount() + qo.getAmount());// 总支付
		channelmapper.put(m);
	}

	/**
	 * 代收新增
	 */
	@Override
	public void totalincome(Income t) {
		RLock lock = RedissonUtil.getLock(t.getQrcodeid());
		try {
			lock.lock();
			Qrcodeaccount ma = mapper.getByQrcodeId(t.getQrcodeid());
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();

			maaj.setUserid(ma.getUserid());
			maaj.setChannelname(t.getQrcodename());
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

			maaj.setRemark("自营代收人民币￥：" + String.format("%.2f", t.getAmount()));
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
	public void updateTotalincome(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount t = mapper.getByQrcodeId(mao.getQrcodeid());
			//
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setChannelname(mao.getQrcodename());
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
			maaj.setRemark("自营代收成功￥：" + String.format("%.2f", mao.getAmount()));
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
	public void cancleTotalincome(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount t = mapper.getByQrcodeId(mao.getQrcodeid());

			//
			Qrcodeaccountrecord maaj = new Qrcodeaccountrecord();
			maaj.setUserid(t.getUserid());
			maaj.setChannelname(mao.getQrcodename());
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
			maaj.setRemark("自营代收取消订单：" + String.format("%.2f", mao.getAmount()));
			qrcodeaccountrecordmapper.post(maaj);
			//
			cancelToincomeamount(t, mao);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================支出
	 * 
	 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void setWithdrawamount(Qrcodeaccount ma, Income mao) {
		// 待支出加金额
		ma.setTowithdrawamount(ma.getTowithdrawamount() + mao.getAmount());
		mapper.put(ma);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void successWithdrawamount(Qrcodeaccount t, Income aaaj) {
		// 待支出减去金额
		t.setTowithdrawamount(t.getTowithdrawamount() - aaaj.getAmount());
		// 支出总金额更新
		t.setWithdrawamount(t.getWithdrawamount() + aaaj.getAmount());

		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Channel m = channelmapper.get(t.getChannelid());
		m.setBalance(t.getBalance());
		channelmapper.put(m);

		qrcodeaccountrecordmapper.post(aaaj);
	}

	// 待确认支出
	@Override
	public void withdrawamount(Income t) {
		RLock lock = RedissonUtil.getLock(t.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount ma = mapper.getByQrcodeId(t.getQrcodeid());
			Qrcodeaccountrecord aaaj = new Qrcodeaccountrecord();

			aaaj.setUserid(ma.getUserid());
			aaaj.setChannelname(t.getQrcodename());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_34);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 待确认支出
			aaaj.setRemark("自营待确认支出￥：" + String.format("%.2f", t.getAmount()));
			//
			qrcodeaccountrecordmapper.post(aaaj);

			setWithdrawamount(ma, t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	public void updateWithdrawamount(Income mao) {
		RLock lock = RedissonUtil.getLock(mao.getChannelid());
		try {
			lock.lock();
			Qrcodeaccount t = mapper.getByQrcodeId(mao.getQrcodeid());
			//
			Qrcodeaccountrecord aaaj = new Qrcodeaccountrecord();
			//
			aaaj.setUserid(t.getUserid());
			aaaj.setChannelname(mao.getQrcodename());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_35);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 待确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmount());// 总支出
			aaaj.setPosttowithdrawamount(mao.getAmount());// 待确认支出
			aaaj.setRemark("自营成功支出￥：" + String.format("%.2f", mao.getAmount()));
			//
			successWithdrawamount(t, mao);
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