package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface AgentaccountorderService extends YtIBaseService<Agentaccountorder, Long> {

	Integer save(Agentaccountorder t);

	// 提现
	void withdrawmanual(Agentaccountorder aco);

	Integer cancleWithdraw(Long id);
}