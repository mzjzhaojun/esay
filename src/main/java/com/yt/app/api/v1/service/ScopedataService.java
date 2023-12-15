package com.yt.app.api.v1.service;

import java.util.List;

import com.yt.app.api.v1.dbo.SysScopeDataBaseDTO;
import com.yt.app.api.v1.entity.Scopedata;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface ScopedataService extends YtIBaseService<Scopedata, Long> {
	public List<Scopedata> tree(SysScopeDataBaseDTO params);
}