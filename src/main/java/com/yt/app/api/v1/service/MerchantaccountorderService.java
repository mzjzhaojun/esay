package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface MerchantaccountorderService extends YtIBaseService<Merchantaccountorder, Long> {

	Integer save(Merchantaccountorder t);

	Integer appsave(Merchantaccountorder t);

	// 充值
	void incomemanual(Merchantaccountorder mco);

	Integer cancle(Long id);

	// 提现
	void withdrawmanual(Merchantaccountorder mco);

	Integer cancleWithdraw(Long id);
}