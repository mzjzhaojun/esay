package com.yt.app.api.v1.service;

import java.util.List;

import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

public interface SysconfigService extends YtIBaseService<Sysconfig, Long> {

	Sysconfig getData();

	List<Sysconfig> getDataTop();

	void initExchangeData();

	List<Sysconfig> getAliPayDataTop();
}