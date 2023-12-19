package com.yt.app.api.v1.service;

import com.yt.app.api.v1.entity.Banks;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-19 13:11:56
 */

public interface BanksService extends YtIBaseService<Banks, Long> {
	void initdata();
}