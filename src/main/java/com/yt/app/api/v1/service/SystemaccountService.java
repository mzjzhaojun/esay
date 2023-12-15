package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */

public interface SystemaccountService extends YtIBaseService<Systemaccount, Long> {

	Systemaccount getData();

	void updateTotalincome(Merchantaccountorder mao);

	void updateWithdrawamount(Merchantaccountorder mao);

	void updateWithdrawamount(Channelaccountorder mao);

	void updateWithdrawamount(Agentaccountorder mao);

	// 代付
	void updatePayout(Merchantaccountorder mao);
}