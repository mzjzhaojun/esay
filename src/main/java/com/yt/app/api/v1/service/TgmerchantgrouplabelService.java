package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmerchantgrouplabel;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

public interface TgmerchantgrouplabelService extends YtIBaseService<Tgmerchantgrouplabel, Long> {
	YtIPage<Tgmerchantgrouplabel> page(Map<String, Object> param);
}