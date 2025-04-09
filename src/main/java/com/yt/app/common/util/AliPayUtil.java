package com.yt.app.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;

import java.util.ArrayList;
import java.util.List;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.SettleDetailInfo;
import com.alipay.api.domain.SettleInfo;
import com.alipay.api.domain.SubMerchant;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.yt.app.api.v1.entity.Qrcode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliPayUtil {

	private static String URL = "https://openapi.alipay.com/gateway.do";

	// ===========================alipay============

	public static AlipayTradeWapPayResponse AlipayTradeWapPay(Qrcode qrcode, String ordernum, Double amount) {

		try {
			AlipayClient client = new DefaultAlipayClient(URL, qrcode.getAppid(), qrcode.getAppprivatekey(), AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8, qrcode.getAlipaypublickey(), AlipayConstants.SIGN_TYPE_RSA2);
			AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			request.setNotifyUrl(qrcode.getNotifyurl());
			model.setOutTradeNo(ordernum);
			model.setTotalAmount(amount.toString());
			model.setSubject("Member Payment");
			model.setProductCode("QUICK_WAP_WAY");
			if (qrcode.getPid() != null) {
				SubMerchant subMerchant = new SubMerchant();
				subMerchant.setMerchantId(qrcode.getSmid());
				model.setSubMerchant(subMerchant);
				SettleInfo settleInfo = new SettleInfo();
				List<SettleDetailInfo> settleDetailInfos = new ArrayList<SettleDetailInfo>();
				SettleDetailInfo settleDetailInfos0 = new SettleDetailInfo();
				settleDetailInfos0.setTransInType("defaultSettle");
				settleDetailInfos0.setAmount(amount.toString());
				settleDetailInfos.add(settleDetailInfos0);
				settleInfo.setSettleDetailInfos(settleDetailInfos);
				model.setSettleInfo(settleInfo);
				ExtendParams extendParams = new ExtendParams();
				extendParams.setSpecifiedSellerName(qrcode.getName());
				extendParams.setRoyaltyFreeze("false");
				model.setExtendParams(extendParams);
			}
			request.setBizModel(model);
			AlipayTradeWapPayResponse response = client.pageExecute(request, "GET");
			if (response.isSuccess()) {
				log.info(" 支付宝创建订单返回消息：" + response.getBody());
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
