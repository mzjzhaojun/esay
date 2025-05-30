package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface ChannelaccountorderService extends YtIBaseService<Channelaccountorder, Long> {

	YtIPage<Channelaccountorder> page(Map<String, Object> param);

	void incomemanual(Channelaccountorder cco);

}