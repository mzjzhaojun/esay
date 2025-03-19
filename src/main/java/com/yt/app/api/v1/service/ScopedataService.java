package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysScopeDataBaseDTO;
import com.yt.app.api.v1.entity.Scopedata;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface ScopedataService extends YtIBaseService<Scopedata, Long> {

	YtIPage<Scopedata> page(Map<String, Object> param);

	public List<Scopedata> tree(SysScopeDataBaseDTO params);
}