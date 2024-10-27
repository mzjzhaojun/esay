package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tenant;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-23 20:33:10
 */

public interface TenantService extends YtIBaseService<Tenant, Long> {
	
	YtIPage<Tenant> page(Map<String, Object> param);
}