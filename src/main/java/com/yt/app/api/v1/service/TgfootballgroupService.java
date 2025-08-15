package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgfootballgroup;
import com.yt.app.api.v1.vo.TgfootballgroupVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-15 17:34:22
 */

public interface TgfootballgroupService extends YtIBaseService<Tgfootballgroup, Long> {
	YtIPage<TgfootballgroupVO> page(Map<String, Object> param);
}