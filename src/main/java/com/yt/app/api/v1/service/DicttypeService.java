package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Dicttype;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DicttypeService extends YtIBaseService<Dicttype, Long> {
	YtIPage<Dicttype> page(Map<String, Object> param);
}