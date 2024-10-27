package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Sysbank;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 11:28:36
 */

public interface SysbankService extends YtIBaseService<Sysbank, Long> {
	YtIPage<Sysbank> page(Map<String, Object> param);
}