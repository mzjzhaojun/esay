package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgbotgroup;
import com.yt.app.api.v1.vo.TgbotgroupVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */

public interface TgbotgroupService extends YtIBaseService<Tgbotgroup, Long> {
	YtIPage<TgbotgroupVO> page(Map<String, Object> param);
}