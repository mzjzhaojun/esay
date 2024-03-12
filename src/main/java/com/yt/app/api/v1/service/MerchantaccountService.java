package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface MerchantaccountService extends YtIBaseService<Merchantaccount, Long> {

	Merchantaccount getData();

	Merchantaccount getDataBank();

	Merchantaccount getDataBank(Long id);

	// 收入
	void totalincome(Merchantaccountorder t);

	void updateTotalincome(Merchantaccountorder mao);

	void turndownTotalincome(Merchantaccountorder mao);

	void cancleTotalincome(Merchantaccountorder mao);

	// 提现
	void withdrawamount(Merchantaccountorder t);

	void updateWithdrawamount(Merchantaccountorder mao);

	void turndownWithdrawamount(Merchantaccountorder mao);

	void cancleWithdrawamount(Merchantaccountorder mao);

	// 代付
	void payout(Merchantaccountorder t);

	void updatePayout(Merchantaccountorder mao);

	void turndownPayout(Merchantaccountorder mao);

	void canclePayout(Merchantaccountorder mao);

	// 代付
	void exchange(Merchantaccountorder t);

	void updateExchange(Merchantaccountorder mao);

	void turndownExchange(Merchantaccountorder mao);

	void cancleExchange(Merchantaccountorder mao);
}