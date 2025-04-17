package com.yt.app.common.util;

import cn.hutool.json.JSONObject;

public class PaymentHelper {

	/**
	 * 协议支付绑卡预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject bindCard(String geteway, String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 协议支付绑卡确认
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject bindCardConfirm(String geteway, String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 协议支付预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject protocolPayPre(String geteway, String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 支付结果查询
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject paymentQuery(String geteway, String request, String signNO, String privatekey) throws Exception {
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}
}
