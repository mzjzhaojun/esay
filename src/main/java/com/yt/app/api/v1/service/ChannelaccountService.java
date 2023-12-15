package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ChannelaccountService extends YtIBaseService<Channelaccount, Long> {
	// 收入
	void totalincome(Channelaccountorder t);

	void updateTotalincome(Channelaccountorder mao);

	void turndownTotalincome(Channelaccountorder mao);

	void cancleTotalincome(Channelaccountorder mao);

	// 支出
	void withdrawamount(Channelaccountorder t);

	void updateWithdrawamount(Channelaccountorder mao);

	void turndownWithdrawamount(Channelaccountorder mao);

	void cancleWithdrawamount(Channelaccountorder mao);
}