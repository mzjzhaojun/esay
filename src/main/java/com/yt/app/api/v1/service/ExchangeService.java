package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.dbo.SysSubmitDTO;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.vo.ExchangeVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */

public interface ExchangeService extends YtIBaseService<Exchange, Long> {

	YtBody tycallbackpay(SysTyOrder so);

	void exchangemanual(Exchange pt);

	//
	Exchange query(String ordernum);

	Exchange submit(SysSubmitDTO ss);

	void submit(String ordernum);

	YtIPage<ExchangeVO> page(Map<String, Object> param);
}