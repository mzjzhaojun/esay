package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.api.v1.vo.SysokxVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-30 13:30:55
 */

public interface SysokxService extends YtIBaseService<Sysokx, Long> {
	YtIPage<SysokxVO> page(Map<String, Object> param);
}