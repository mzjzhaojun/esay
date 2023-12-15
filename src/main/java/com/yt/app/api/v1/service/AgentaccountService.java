package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface AgentaccountService extends YtIBaseService<Agentaccount, Long> {

	Agentaccount getData();

	Agentaccount getDataBank();

	Agentaccount getDataBank(Long id);

	// 收入
	void totalincome(Agentaccountorder t);

	void updateTotalincome(Agentaccountorder mao);

	void turndownTotalincome(Agentaccountorder mao);

	void cancleTotalincome(Agentaccountorder mao);

	// 提现
	void withdrawamount(Agentaccountorder t);

	void updateWithdrawamount(Agentaccountorder mao);

	void turndownWithdrawamount(Agentaccountorder mao);

	void cancleWithdrawamount(Agentaccountorder mao);
}