package com.yt.app.common.util;

public class PaymentHelper {

	static String bindCard = "https://test-efps.epaylinks.cn/api/txs/protocol/bindCard";
	static String bindCardConfirm = "https://test-efps.epaylinks.cn/api/txs/protocol/bindCardConfirm";

	/**
	 * 协议支付绑卡预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static String bindCard(String request) throws Exception {
		return RemoteInvoker.invoke(request, bindCard);
	}

	/**
	 * 协议支付绑卡确认
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static String bindCardConfirm(String request, String keyurl, String signno) throws Exception {
		return RemoteInvoker.invoke(request, bindCardConfirm);
	}

}
