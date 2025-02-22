package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */

public interface ChannelService extends YtIBaseService<Channel, Long> {

	YtIPage<Channel> page(Map<String, Object> param);

	//
	Integer getRemotebalance(Long id);

	//
	Channel getData();

	//
	void updateDayValue(Channel c, String date);
}