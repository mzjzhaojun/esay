package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Agentaccountrecord;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:44:01
 */

public interface AgentaccountrecordService extends YtIBaseService<Agentaccountrecord, Long> {
	YtIPage<Agentaccountrecord> page(Map<String, Object> param);
}