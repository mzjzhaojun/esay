package com.yt.app.api.v1.service;

import java.util.List;

import com.yt.app.api.v1.dbo.SysProvinceCityAreaTreeDTO;
import com.yt.app.api.v1.entity.Provincecityarea;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-03 19:50:02
 */

public interface ProvincecityareaService extends YtIBaseService<Provincecityarea, Long> {
	public List<Provincecityarea> tree(SysProvinceCityAreaTreeDTO params);
}