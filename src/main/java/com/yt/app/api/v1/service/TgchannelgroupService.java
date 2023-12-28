package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */

public interface TgchannelgroupService extends YtIBaseService<Tgchannelgroup, Long> {

	Integer putchannel(Tgchannelgroup t);
}