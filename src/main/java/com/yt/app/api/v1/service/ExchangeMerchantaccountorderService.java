package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountorderService extends YtIBaseService<ExchangeMerchantaccountorder, Long> {
	
	YtIPage<ExchangeMerchantaccountorder> page(Map<String, Object> param);

	Integer save(ExchangeMerchantaccountorder t);

	Integer appsave(ExchangeMerchantaccountorder t);

	// 充值
	void incomemanual(ExchangeMerchantaccountorder mco);

	Integer cancle(Long id);

	// 提现
	void withdrawmanual(ExchangeMerchantaccountorder mco);

	Integer cancleWithdraw(Long id);
}