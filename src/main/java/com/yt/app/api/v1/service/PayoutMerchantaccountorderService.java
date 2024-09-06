package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountorderService extends YtIBaseService<PayoutMerchantaccountorder, Long> {

	Integer save(PayoutMerchantaccountorder t);

	Integer appsave(PayoutMerchantaccountorder t);

	// 充值
	void incomemanual(PayoutMerchantaccountorder mco);

	Integer cancle(Long id);

	// 提现
	void withdrawmanual(PayoutMerchantaccountorder mco);

	Integer cancleWithdraw(Long id);

	Integer incomewithdraw(PayoutMerchantaccountorder mco);

	// 代收提现
	void incomewithdrawmanual(PayoutMerchantaccountorder mco);
}