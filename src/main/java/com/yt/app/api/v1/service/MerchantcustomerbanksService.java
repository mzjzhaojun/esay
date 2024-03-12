package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Merchantcustomerbanks;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 18:43:33
 */

public interface MerchantcustomerbanksService extends YtIBaseService<Merchantcustomerbanks, Long> {
	Integer add(Payout t);
}