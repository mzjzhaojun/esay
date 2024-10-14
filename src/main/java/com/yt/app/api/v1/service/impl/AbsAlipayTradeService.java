package com.yt.app.api.v1.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.*;
import com.yt.app.api.v1.model.TradeStatus;
import com.yt.app.api.v1.model.result.AlipayF2FPrecreateResult;
import com.yt.app.api.v1.model.result.AlipayF2FQueryResult;
import com.yt.app.api.v1.service.AlipayTradeService;
import com.yt.app.common.resource.DictionaryResource;

import org.springframework.stereotype.Service;

/**
 * Created by liuyangkly on 15/10/28.
 */
@Service
public class AbsAlipayTradeService implements AlipayTradeService {

	@Override
	public AlipayF2FQueryResult queryTradeResult(AlipayTradeQueryRequest request, AlipayClient client) {

		AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) getResponse(client, request);

		AlipayF2FQueryResult result = new AlipayF2FQueryResult(response);

		if (querySuccess(response)) {
			// 查询返回该订单交易支付成功
			result.setTradeStatus(TradeStatus.SUCCESS);

		} else if (tradeError(response)) {
			// 查询发生异常，交易状态未知
			result.setTradeStatus(TradeStatus.UNKNOWN);

		} else {
			// 其他情况均表明该订单号交易失败
			result.setTradeStatus(TradeStatus.FAILED);
		}
		return result;
	}

	@Override
	public AlipayF2FPrecreateResult tradePrecreate(AlipayTradePrecreateRequest request, AlipayClient client) {

		AlipayTradePrecreateResponse response = (AlipayTradePrecreateResponse) getResponse(client, request);

		AlipayF2FPrecreateResult result = new AlipayF2FPrecreateResult(response);
		if (response != null && DictionaryResource.SUCCESS.equals(response.getCode())) {
			// 预下单交易成功
			result.setTradeStatus(TradeStatus.SUCCESS);

		} else if (tradeError(response)) {
			// 预下单发生异常，状态未知
			result.setTradeStatus(TradeStatus.UNKNOWN);

		} else {
			// 其他情况表明该预下单明确失败
			result.setTradeStatus(TradeStatus.FAILED);
		}
		return result;
	}

	// 交易异常，或发生系统错误
	protected boolean tradeError(AlipayResponse response) {
		return response == null || DictionaryResource.ERROR.equals(response.getCode());
	}

	// 查询返回“支付成功”
	protected boolean querySuccess(AlipayTradeQueryResponse response) {
		return response != null && DictionaryResource.SUCCESS.equals(response.getCode()) && ("TRADE_SUCCESS".equals(response.getTradeStatus()) || "TRADE_FINISHED".equals(response.getTradeStatus()));
	}

	// 调用AlipayClient的execute方法，进行远程调用
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected AlipayResponse getResponse(AlipayClient client, AlipayRequest request) {
		try {
			AlipayResponse response = client.certificateExecute(request);
			return response;

		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
	}
}
