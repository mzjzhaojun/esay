package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgchannelgrouplabel;
import com.yt.app.api.v1.vo.TgchannelgrouplabelVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */

public interface TglabelService extends YtIBaseService<Tgchannelgrouplabel, Long> {
	YtIPage<TgchannelgrouplabelVO> page(Map<String, Object> param);
}