package com.yt.app.common.util;

import com.alipay.api.AlipayClient;

import java.util.ArrayList;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.yt.app.api.v1.entity.Qrcode;

public class AliPayUtil {

	private static String URL = "https://openapi.alipay.com/gateway.do";

	// ===========================alipay============

	public static AlipayTradeWapPayResponse AlipayTradeWapPay(Qrcode qrcode, String ordernum, Double amount) {

		try {
			// 初始化SDK
			AlipayClient client = new DefaultAlipayClient(URL, qrcode.getAppid(), qrcode.getAppprivatekey(), "json", "UTF-8", qrcode.getAlipaypublickey(), "RSA2");
			// 构造请求参数以调用接口
			AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			request.setNotifyUrl(qrcode.getNotifyurl());
			// 设置商户订单号
			model.setOutTradeNo(ordernum);
			// 设置订单总金额
			model.setTotalAmount(amount.toString());
			// 设置订单标题
			model.setSubject("会员支付");
			// 设置产品码
			model.setProductCode("QUICK_WAP_WAY");
			request.setBizModel(model);
			// 如果需要返回GET请求，请使用
			AlipayTradeWapPayResponse response = client.pageExecute(request, "GET");
			String pageRedirectionData = response.getBody();
			System.out.println(pageRedirectionData);
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AlipayTradeQueryResponse AlipayTradeWapQuery(Qrcode qrcode, String outno, String ordernum) {
		try {
			AlipayClient client = new DefaultAlipayClient(URL, qrcode.getAppid(), qrcode.getAppprivatekey(), "json", "UTF-8", qrcode.getAlipaypublickey(), "RSA2");
			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(outno);
			model.setTradeNo(ordernum);
			List<String> queryOptions = new ArrayList<String>();
			queryOptions.add("trade_settle_info");
			model.setQueryOptions(queryOptions);
			request.setBizModel(model);
			AlipayTradeQueryResponse response = client.execute(request);
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}
}
