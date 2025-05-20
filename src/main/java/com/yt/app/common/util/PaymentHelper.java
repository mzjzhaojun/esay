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
		geteway = geteway + "/api/txs/protocol/bindCard";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 协议支付绑卡确认
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject bindCardConfirm(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs/protocol/bindCardConfirm";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 协议支付预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject protocolPayPre(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs/protocol/protocolPayPre";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 支付结果查询
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject paymentQuery(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs/pay/PaymentQuery";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 单笔代付
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject withdrawalToCard(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs/pay/withdrawalToCard";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}

	/**
	 * 单笔代付查询
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject withdrawalToCardQuery(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs/pay/withdrawalToCardQuery";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}
	
	
	/**
	 * 单笔代付凭证
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject withdrawalCertification(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/txs-query/pay/withdrawalCertification";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}
	

	/**
	 * 余额查询
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static JSONObject accountQuery(String geteway, String request, String signNO, String privatekey) throws Exception {
		geteway = geteway + "/api/acc/accountQuery";
		return RemoteInvoker.invoke(request, geteway, signNO, privatekey);
	}
}
