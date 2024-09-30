package com.yt.app.api.v1.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.yt.app.api.v1.model.result.AlipayF2FPrecreateResult;
import com.yt.app.api.v1.model.result.AlipayF2FQueryResult;

/**
 * Created by liuyangkly on 15/7/29.
 */
public interface AlipayTradeService {


    // 当面付2.0消费查询
    public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequest request, AlipayClient client);

	// 当面付2.0预下单(生成二维码)
	public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequest request, AlipayClient client);
}
