package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountService extends YtIBaseService<PayoutMerchantaccount, Long> {

	PayoutMerchantaccount getData();

	PayoutMerchantaccount getDataBank();

	PayoutMerchantaccount getDataBank(Long id);

	// 收入
	void totalincome(PayoutMerchantaccountorder t);

	void updateTotalincome(PayoutMerchantaccountorder mao);

	void turndownTotalincome(PayoutMerchantaccountorder mao);

	void cancleTotalincome(PayoutMerchantaccountorder mao);

	// 提现
	void withdrawamount(PayoutMerchantaccountorder t);

	void updateWithdrawamount(PayoutMerchantaccountorder mao);

	void turndownWithdrawamount(PayoutMerchantaccountorder mao);

	void cancleWithdrawamount(PayoutMerchantaccountorder mao);

	// 代付
	void payout(PayoutMerchantaccountorder t);

	void updatePayout(PayoutMerchantaccountorder mao);

	void failPayout(PayoutMerchantaccountorder mao);

	void turndownPayout(PayoutMerchantaccountorder mao);

	void canclePayout(PayoutMerchantaccountorder mao);

	// 换汇
	void exchange(PayoutMerchantaccountorder t);

	void updateExchange(PayoutMerchantaccountorder mao);

	void turndownExchange(PayoutMerchantaccountorder mao);

	void cancleExchange(PayoutMerchantaccountorder mao);
}