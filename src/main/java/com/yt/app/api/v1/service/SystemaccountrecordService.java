package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Systemaccountrecord;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */

public interface SystemaccountrecordService extends YtIBaseService<Systemaccountrecord, Long> {
	YtIPage<Systemaccountrecord> page(Map<String, Object> param);
}