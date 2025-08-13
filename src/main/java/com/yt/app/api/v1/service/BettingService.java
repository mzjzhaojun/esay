package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.vo.BettingVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:16
 */

public interface BettingService extends YtIBaseService<Betting, Long> {
	YtIPage<BettingVO> page(Map<String, Object> param);
}