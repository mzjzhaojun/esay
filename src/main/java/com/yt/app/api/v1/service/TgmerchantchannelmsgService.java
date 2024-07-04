package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.vo.TgmerchantchannelmsgVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-04 16:47:48
 */

public interface TgmerchantchannelmsgService extends YtIBaseService<Tgmerchantchannelmsg, Long> {
	YtIPage<TgmerchantchannelmsgVO> page(Map<String, Object> param);
}