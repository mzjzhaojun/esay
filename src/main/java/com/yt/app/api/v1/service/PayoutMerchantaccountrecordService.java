package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.PayoutMerchantaccountrecord;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountrecordService extends YtIBaseService<PayoutMerchantaccountrecord, Long> {
	YtIPage<PayoutMerchantaccountrecord> page(Map<String, Object> param);
}