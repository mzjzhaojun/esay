package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface AgentaccountService extends YtIBaseService<Agentaccount, Long> {

	YtIPage<Agentaccount> page(Map<String, Object> param);

	Agentaccount getData();

	Agentaccount getDataBank();

	Agentaccount getDataBank(Long id);

	// 代收收入
	void totalincome(Income t);

	void updateTotalincome(Income t);

	void turndownTotalincome(Income t);

	void cancleTotalincome(Income t);

	// 代付收入
	void totalincome(Payout t);

	void updateTotalincome(Payout t);

	void turndownTotalincome(Payout t);

	void cancleTotalincome(Payout t);
}