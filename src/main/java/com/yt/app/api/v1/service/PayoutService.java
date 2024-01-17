package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

public interface PayoutService extends YtIBaseService<Payout, Long> {

	void callbackpaySuccess(Payout pt);

	void callbackpayFail(Payout pt);

	void tycallbackpay(SysTyOrder so);

	void paySuccess(Payout pt);

	Payout query(String ordernum);
}