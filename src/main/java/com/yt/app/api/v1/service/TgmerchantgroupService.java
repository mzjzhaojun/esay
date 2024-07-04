package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.vo.TgmerchantgroupVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 17:05:13
 */

public interface TgmerchantgroupService extends YtIBaseService<Tgmerchantgroup, Long> {
	YtIPage<TgmerchantgroupVO> page(Map<String, Object> param);

	public Integer putmerchant(Tgmerchantgroup t);
}