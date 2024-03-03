package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */

public interface AgentService extends YtIBaseService<Agent, Long> {
	void updateWithdraw(Agentaccount t);

	void updatePayout(Payout t);
	
	void updateIncome(Agentaccount t);
}