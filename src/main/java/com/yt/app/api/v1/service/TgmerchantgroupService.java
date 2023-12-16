package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

public interface TgmerchantgroupService extends YtIBaseService<Tgmerchantgroup, Long> {
	Integer putmerchant(Tgmerchantgroup t);
}