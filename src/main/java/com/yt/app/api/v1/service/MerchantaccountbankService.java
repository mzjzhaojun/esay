package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:42:46
 */

public interface MerchantaccountbankService extends YtIBaseService<Merchantaccountbank, Long> {
	YtIPage<Merchantaccountbank> page(Map<String, Object> param);
}