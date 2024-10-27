package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

public interface SysconfigService extends YtIBaseService<Sysconfig, Long> {
	
	YtIPage<Sysconfig> page(Map<String, Object> param);

	Sysconfig getUsdtExchangeData();

	Sysconfig getUsdtToTrxExchangeData();

	List<Sysconfig> getDataTop();

	void initSystemData();

	List<Sysconfig> getAliPayDataTop();
}