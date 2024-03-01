package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Twitter;
import com.yt.app.api.v1.vo.TwitterVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-02-26 11:42:42
 */

public interface TwitterService extends YtIBaseService<Twitter, Long> {
	YtIPage<TwitterVO> page(Map<String, Object> param);
}