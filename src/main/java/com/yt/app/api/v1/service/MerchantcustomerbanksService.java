package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Merchantcustomerbanks;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 18:43:33
 */

public interface MerchantcustomerbanksService extends YtIBaseService<Merchantcustomerbanks, Long> {
	
	YtIPage<Merchantcustomerbanks> page(Map<String, Object> param);

	Integer add(Payout t);

	Integer add(Exchange t);
}