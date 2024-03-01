package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmerchantgroupmessage;
import com.yt.app.api.v1.vo.TgmerchantgroupmessageVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-02-26 11:59:44
 */

public interface TgmerchantgroupmessageService extends YtIBaseService<Tgmerchantgroupmessage, Long> {
	YtIPage<TgmerchantgroupmessageVO> page(Map<String, Object> param);
}