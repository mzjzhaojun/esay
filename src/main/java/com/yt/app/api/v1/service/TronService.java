package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 15:25:57
 */

public interface TronService extends YtIBaseService<Tron, Long> {

	Tron get();

	YtIPage<TronVO> page(Map<String, Object> param);

}