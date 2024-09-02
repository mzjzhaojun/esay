package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Channelstatisticalreports;
import com.yt.app.api.v1.vo.ChannelstatisticalreportsVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:03:33
 */

public interface ChannelstatisticalreportsService extends YtIBaseService<Channelstatisticalreports, Long> {
	YtIPage<ChannelstatisticalreportsVO> page(Map<String, Object> param);
}