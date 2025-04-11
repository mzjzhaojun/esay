package com.yt.app.common.util;

import com.yt.app.common.util.bo.ProtocolPayBindCardConfirmRequest;
import com.yt.app.common.util.bo.ProtocolPayBindCardConfirmResponse;
import com.yt.app.common.util.bo.ProtocolPayBindCardRequest;
import com.yt.app.common.util.bo.ProtocolPayBindCardResponse;

public class PaymentHelper {

	static String bindCard = "https://efps.epaylinks.cn/api/txs/protocol/bindCard";
	static String bindCardConfirm = "https://efps.epaylinks.cn/api/txs/protocol/bindCardConfirm";

	/**
	 * 协议支付绑卡预交易
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static ProtocolPayBindCardResponse bindCard(ProtocolPayBindCardRequest request, String keyurl, String signno, String ukey, String passwwrod) throws Exception {
		return RemoteInvoker.invoke(request, bindCard, keyurl, signno, ProtocolPayBindCardResponse.class);
	}

	/**
	 * 协议支付绑卡确认
	 * 
	 * @param request 订单内容
	 * @throws Exception
	 */
	public static ProtocolPayBindCardConfirmResponse bindCardConfirm(ProtocolPayBindCardConfirmRequest request, String keyurl, String signno, String ukey, String passwwrod) throws Exception {
		return RemoteInvoker.invoke(request, bindCardConfirm, keyurl, signno, ProtocolPayBindCardConfirmResponse.class);
	}

}
