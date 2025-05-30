package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */

public interface SystemaccountService extends YtIBaseService<Systemaccount, Long> {

	YtIPage<Systemaccount> page(Map<String, Object> param);

	Systemaccount getData();

	// 代付成功
	void updatePayout(Payout mao);

	// 代收成功收款
	void updateIncome(Income mao);

}