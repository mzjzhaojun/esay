package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgbot;
import com.yt.app.api.v1.vo.TgbotVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */

public interface TgbotService extends YtIBaseService<Tgbot, Long> {
	YtIPage<TgbotVO> page(Map<String, Object> param);
}