package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Dict;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DictService extends YtIBaseService<Dict, Long> {

	List<Dict> listbycode(Map<String, Object> param);

	List<Dict> listfromcachebycode(String code);

	void initCache();
}