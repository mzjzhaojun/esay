package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.vo.QrcodeaccountVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

public interface QrcodeaccountService extends YtIBaseService<Qrcodeaccount, Long> {
	YtIPage<QrcodeaccountVO> page(Map<String, Object> param);

	/**
	 * 代收待确认
	 * 
	 * @param t
	 */
	void totalincome(Income t);

	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Income mao);

	/**
	 * 超时支付取消订单
	 * 
	 * @param mao
	 */
	void cancleTotalincome(Income mao);

	/**
	 * 待确认支出
	 * 
	 * @param t
	 */
	void withdrawamount(Payout t);

	/**
	 * 确认支出
	 * 
	 * @param mao
	 */
	void updateWithdrawamount(Payout mao);

}