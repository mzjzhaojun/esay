package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
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
	void updateTotalincome(PayoutMerchantaccountorder mao);

	// 商户提现
	void updateWithdrawamount(PayoutMerchantaccountorder mao);

	// 代理提现
	void updateWithdrawamount(Agentaccountorder mao);

	// 代付
	void updatePayout(PayoutMerchantaccountorder mao);

	// 换汇
	void updateExchange(ExchangeMerchantaccountorder mao);

	// 代收
	void updateIncome(Incomemerchantaccountorder mao);

	// 商户充值
	void updateUsdtTotalincome(ExchangeMerchantaccountorder mao);

	// 商户提现
	void updateUsdtWithdrawamount(ExchangeMerchantaccountorder mao);

}