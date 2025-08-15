package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.vo.CrownagentVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */

public interface CrownagentService extends YtIBaseService<Crownagent, Long> {
	YtIPage<CrownagentVO> page(Map<String, Object> param);

	// 更新登录
	Integer login(Crownagent t);
}