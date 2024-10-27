package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */

public interface AisleService extends YtIBaseService<Aisle, Long> {
	YtIPage<Aisle> page(Map<String, Object> param);
}