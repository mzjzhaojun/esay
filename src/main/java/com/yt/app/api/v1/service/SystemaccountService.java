package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agentaccountorder;
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

	// 商户充值
	void updateTotalincome(Merchantaccountorder mao);

	// 商户提现
	void updateWithdrawamount(Merchantaccountorder mao);

	// 代理提现
	void updateWithdrawamount(Agentaccountorder mao);

	// 代付
	void updatePayout(Merchantaccountorder mao);

	// 换汇
	void updateExchange(Merchantaccountorder mao);
}