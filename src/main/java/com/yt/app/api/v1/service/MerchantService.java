package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */

public interface MerchantService extends YtIBaseService<Merchant, Long> {

	Integer putagent(Merchant m);

	void updatePayout(Payout t);

	void updateExchange(Exchange t);

	void updateInCome(Merchantaccount t);

	void withdrawamount(Merchantaccount t);

	void updateInComeUsdt(ExchangeMerchantaccount t);

	void withdrawamountUsdt(ExchangeMerchantaccount t);

	Merchant getData();
}