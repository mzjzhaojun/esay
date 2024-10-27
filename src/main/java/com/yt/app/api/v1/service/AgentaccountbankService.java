package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Agentaccountbank;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:39:42
 */

public interface AgentaccountbankService extends YtIBaseService<Agentaccountbank, Long> {
	YtIPage<Agentaccountbank> page(Map<String, Object> param);
}