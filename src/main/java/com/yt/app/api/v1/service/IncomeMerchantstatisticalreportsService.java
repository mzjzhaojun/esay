package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.IncomeMerchantstatisticalreports;
import com.yt.app.api.v1.vo.IncomeMerchantstatisticalreportsVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:01:51
 */

public interface IncomeMerchantstatisticalreportsService extends YtIBaseService<IncomeMerchantstatisticalreports, Long> {
	YtIPage<IncomeMerchantstatisticalreportsVO> page(Map<String, Object> param);
}