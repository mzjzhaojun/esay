package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Payoutmerchantstatisticalreports;
import com.yt.app.api.v1.vo.PayoutmerchantstatisticalreportsVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-06-07 23:16:10
 */

public interface PayoutmerchantstatisticalreportsService extends YtIBaseService<Payoutmerchantstatisticalreports, Long> {
	YtIPage<PayoutmerchantstatisticalreportsVO> page(Map<String, Object> param);
}