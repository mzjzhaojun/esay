package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface ChannelaccountorderService extends YtIBaseService<Channelaccountorder, Long> {

	void incomemanual(Channelaccountorder cco);

}