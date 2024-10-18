package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Systemstatisticalreports;
import com.yt.app.api.v1.vo.SystemstatisticalreportsVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 14:45:16
 */

public interface SystemstatisticalreportsService extends YtIBaseService<Systemstatisticalreports, Long> {
	YtIPage<SystemstatisticalreportsVO> page(Map<String, Object> param);

	void updateDayValue(String date);
}