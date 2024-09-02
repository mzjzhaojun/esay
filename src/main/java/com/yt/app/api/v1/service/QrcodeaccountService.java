package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
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
	void totalincome(Qrcodeaccountorder t);

	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Qrcodeaccountorder mao);

	/**
	 * 超时支付取消订单
	 * 
	 * @param mao
	 */
	void cancleTotalincome(Qrcodeaccountorder mao);

	Qrcodeaccount getData();
}