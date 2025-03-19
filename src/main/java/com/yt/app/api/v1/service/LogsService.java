package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Logs;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-31 13:31:43
 */

public interface LogsService extends YtIBaseService<Logs, Long> {

	YtIPage<Logs> page(Map<String, Object> param);
}