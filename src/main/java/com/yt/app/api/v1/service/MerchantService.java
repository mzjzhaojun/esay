package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */

public interface MerchantService extends YtIBaseService<Merchant, Long> {

	YtIPage<Merchant> page(Map<String, Object> param);

	Integer putagent(Merchant m);

	Merchant getData();

	void updateIncome(Merchant c, String date);
	
	void updatePayout(Merchant c, String date);

	Merchant postMerchant(Merchant m);

}