package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface AgentaccountorderService extends YtIBaseService<Agentaccountorder, Long> {
	
	YtIPage<Agentaccountorder> page(Map<String, Object> param);

	Integer save(Agentaccountorder t);

	// 提现
	void withdrawmanual(Agentaccountorder aco);

	Integer cancleWithdraw(Long id);
}