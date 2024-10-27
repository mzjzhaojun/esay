package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:15:12
 */

public interface MerchantaisleService extends YtIBaseService<Merchantaisle, Long> {
	YtIPage<Merchantaisle> page(Map<String, Object> param);

}