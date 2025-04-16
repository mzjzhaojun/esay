package com.yt.app.common.util;

import cn.hutool.json.JSONObject;

public class PaymentHelper {

	static String bindCard = "https://test-efps.epaylinks.cn/api/txs/protocol/bindCard";
	static String bindCardConfirm = "https://test-efps.epaylinks.cn/api/txs/protocol/bindCardConfirm";
	static String protocolPayPre = "https://test-efps.epaylinks.cn/api/txs/protocol/protocolPayPre";
	static String paymentQuery = "https://test-efps.epaylinks.cn/api/txs/pay/PaymentQuery";

	/**
	 * 协议支付绑卡预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject bindCard(String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, bindCard, signNO, privatekey);
	}

	/**
	 * 协议支付绑卡确认
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject bindCardConfirm(String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, bindCardConfirm, signNO, privatekey);
	}

	/**
	 * 协议支付预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject protocolPayPre(String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, protocolPayPre, signNO, privatekey);
	}

	/**
	 * 支付结果查询
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject paymentQuery(String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, paymentQuery, signNO, privatekey);
	}
}
