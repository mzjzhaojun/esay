package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountService extends YtIBaseService<ExchangeMerchantaccount, Long> {

	YtIPage<ExchangeMerchantaccount> page(Map<String, Object> param);

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

}