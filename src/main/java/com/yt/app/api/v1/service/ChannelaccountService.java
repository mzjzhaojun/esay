package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ChannelaccountService extends YtIBaseService<Channelaccount, Long> {

	YtIPage<Channelaccount> page(Map<String, Object> param);

	// 收入
	void totalincome(Income t);

	void updateTotalincome(Income t);

	void turndownTotalincome(Income t);

	void cancleTotalincome(Income t);

	// 支出
	void withdrawamount(Payout t);

	void updateWithdrawamount(Payout t);

	void turndownWithdrawamount(Payout t);

	void cancleWithdrawamount(Payout t);

}