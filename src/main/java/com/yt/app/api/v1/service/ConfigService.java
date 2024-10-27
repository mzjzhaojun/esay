package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Config;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-02 20:38:10
 */

public interface ConfigService extends YtIBaseService<Config, Long> {
	YtIPage<Config> page(Map<String, Object> param);
}