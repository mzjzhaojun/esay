package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

public interface PayoutService extends YtIBaseService<Payout, Long> {

	void payoutmanual(Payout pt);

	YtBody tycallbackpay(SysTyOrder so);

	// 代付盘口下单查单
	PayResultVO query(String ordernum);

	PayResultVO queryblance(String merchantid);

	PayResultVO submit(PaySubmitDTO ss);

	YtIPage<PayoutVO> page(Map<String, Object> param);
}