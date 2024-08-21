package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

public interface IncomeService extends YtIBaseService<Income, Long> {
	YtIPage<IncomeVO> page(Map<String, Object> param);
}