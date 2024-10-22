package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */

public interface TgchannelgroupService extends YtIBaseService<Tgchannelgroup, Long> {

	YtIPage<Tgchannelgroup> page(Map<String, Object> param);

	Integer putchannel(Tgchannelgroup t);
}