package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountService extends YtIBaseService<ExchangeMerchantaccount, Long> {

	ExchangeMerchantaccount getData();

	ExchangeMerchantaccount getDataBank();

	ExchangeMerchantaccount getDataBank(Long id);

	// 收入
	void totalincome(ExchangeMerchantaccountorder t);

	void updateTotalincome(ExchangeMerchantaccountorder mao);

	void turndownTotalincome(ExchangeMerchantaccountorder mao);

	void cancleTotalincome(ExchangeMerchantaccountorder mao);

	// 提现
	void withdrawamount(ExchangeMerchantaccountorder t);

	void updateWithdrawamount(ExchangeMerchantaccountorder mao);

	void turndownWithdrawamount(ExchangeMerchantaccountorder mao);

	void cancleWithdrawamount(ExchangeMerchantaccountorder mao);

	// 代付
	void payout(ExchangeMerchantaccountorder t);

	void updatePayout(ExchangeMerchantaccountorder mao);

	void failPayout(ExchangeMerchantaccountorder mao);

	void turndownPayout(ExchangeMerchantaccountorder mao);

	void canclePayout(ExchangeMerchantaccountorder mao);

	// 换汇
	void exchange(ExchangeMerchantaccountorder t);

	void updateExchange(ExchangeMerchantaccountorder mao);

	void turndownExchange(ExchangeMerchantaccountorder mao);

	void cancleExchange(ExchangeMerchantaccountorder mao);
}