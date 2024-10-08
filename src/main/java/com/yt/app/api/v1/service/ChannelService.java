package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */

public interface ChannelService extends YtIBaseService<Channel, Long> {

	void updatePayout(Payout t);

	void updateExchange(Exchange t);

	void updateIncome(Channelaccount t);

	void withdrawamount(Channelaccount t);

	Integer getRemotebalance(Long id);

	Channel getData();

	void updateIncome(Income t);

	//
	void updateDayValue(Channel c, String date);
}