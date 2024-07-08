package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountorderService extends YtIBaseService<ExchangeMerchantaccountorder, Long> {

	Integer save(ExchangeMerchantaccountorder t);

	Integer appsave(ExchangeMerchantaccountorder t);

	// 充值
	void incomemanual(ExchangeMerchantaccountorder mco);

	Integer cancle(Long id);

	// 提现
	void withdrawmanual(ExchangeMerchantaccountorder mco);

	Integer cancleWithdraw(Long id);
}