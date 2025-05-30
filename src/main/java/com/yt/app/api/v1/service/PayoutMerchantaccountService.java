package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountService extends YtIBaseService<PayoutMerchantaccount, Long> {

	YtIPage<PayoutMerchantaccount> page(Map<String, Object> param);

	PayoutMerchantaccount getData();

	PayoutMerchantaccount getDataBank();

	PayoutMerchantaccount getDataBank(Long id);

	// 收入
	void totalincome(Payout t);

	void updateTotalincome(Payout t);

	void turndownTotalincome(Payout t);

	void cancleTotalincome(Payout t);

	// 提现
	void withdrawamount(Payout t);

	void updateWithdrawamount(Payout t);

	void turndownWithdrawamount(Payout t);

	void cancleWithdrawamount(Payout t);

}