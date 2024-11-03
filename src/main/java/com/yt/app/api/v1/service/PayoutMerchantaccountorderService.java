package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountorderService extends YtIBaseService<PayoutMerchantaccountorder, Long> {

	YtIPage<PayoutMerchantaccountorder> page(Map<String, Object> param);

	Integer save(PayoutMerchantaccountorder t);

	Integer appsave(PayoutMerchantaccountorder t);

	void incomemanual(PayoutMerchantaccountorder mco);

	Integer cancle(Long id);

	// 提现
	void withdrawmanual(PayoutMerchantaccountorder mco);

	Integer cancleWithdraw(Long id);

}