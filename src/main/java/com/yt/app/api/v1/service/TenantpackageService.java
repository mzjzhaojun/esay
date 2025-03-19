package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tenantpackage;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-01 20:08:23
 */

public interface TenantpackageService extends YtIBaseService<Tenantpackage, Long> {

	YtIPage<Tenantpackage> page(Map<String, Object> param);
}