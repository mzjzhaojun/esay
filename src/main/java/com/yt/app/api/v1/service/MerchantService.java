package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */

public interface MerchantService extends YtIBaseService<Merchant, Long> {
	
	YtIPage<Merchant> page(Map<String, Object> param);

	Integer putagent(Merchant m);

	// 代收
	void updateIncome(Income t);

	// 代付
	void updatePayout(Payout t);

	// 换汇
	void updateExchange(Exchange t);

	void updateInCome(PayoutMerchantaccount t);

	void withdrawamount(PayoutMerchantaccount t);

	void updateInComeUsdt(ExchangeMerchantaccount t);

	void withdrawamountUsdt(ExchangeMerchantaccount t);

	Merchant getData();

	void updateDayValue(Merchant c, String date);

	// 代收
	void withdrawamount(Incomemerchantaccount ma);
}