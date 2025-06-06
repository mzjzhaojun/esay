package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Merchantaccountorder;
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

	// 代付
	void withdrawamount(Payout t);

	// 代付成功
	void updateWithdrawamount(Payout t);

	// 代付失败
	void cancleWithdrawamount(Payout t);

	// 充值
	void totalincome(Merchantaccountorder mco);

	// 充值成功
	void updateTotalincome(Merchantaccountorder mco);

	// 充值失敗
	void cancleTotalincome(Merchantaccountorder mco);

	// 代付提现
	void withdrawamount(Merchantaccountorder mco);

	// 代付提现成功
	void updateWithdrawamount(Merchantaccountorder mco);

	// 代付提现失败
	void cancleWithdrawamount(Merchantaccountorder mco);

}