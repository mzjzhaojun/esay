package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */

public interface SystemaccountService extends YtIBaseService<Systemaccount, Long> {

	YtIPage<Systemaccount> page(Map<String, Object> param);

	Systemaccount getData();

	// 商户充值
	void updateTotalincome(PayoutMerchantaccountorder mao);

	// 商户提现
	void updateWithdrawamount(PayoutMerchantaccountorder mao);

	// 代理提现
	void updateWithdrawamount(Agentaccountorder mao);

	// 代付成功
	void updatePayout(PayoutMerchantaccountorder mao);


	// 代收提现
	void updateIncome(Income mao);
	
	// 代收充值
	void updateIncome(Incomemerchantaccountorder mao);


}