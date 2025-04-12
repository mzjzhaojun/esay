package com.yt.app.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import com.yt.app.common.util.bo.ProtocolPayBindCardRequest;
import com.yt.app.common.util.bo.ProtocolPayBindCardResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelfPayUtil {

	private static String URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * 支付宝创建订单
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
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

	/**
	 * 支付宝查单
	 * 
	 * @param qrcode
	 * @param outno
	 * @param ordernum
	 * @return
	 */
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

	/**
	 * 易票联创建订单
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static ProtocolPayBindCardResponse eplpayTradeWapPay() {
		try {

			String mchtOrderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // 交易编号,商户侧唯一

			String publicKeyPath = "C:\\Users\\zj\\Downloads\\java\\bin\\efps_new.cer";
			String userName = RsaUtils.encryptByPublicKey("银联一", RsaUtils.getPublicKey(publicKeyPath));
			String certificatesNo = RsaUtils.encryptByPublicKey("500381198804159412", RsaUtils.getPublicKey(publicKeyPath));
			String bankCardNo = RsaUtils.encryptByPublicKey("621904126549878596", RsaUtils.getPublicKey(publicKeyPath));
			String phoneNum = RsaUtils.encryptByPublicKey("13430293947", RsaUtils.getPublicKey(publicKeyPath));
			// 6212262011222352668 6225882014767005
			ProtocolPayBindCardRequest request = new ProtocolPayBindCardRequest();
			request.setVersion("2.0");
			request.setCustomerCode("562265003122220");
			request.setMemberId("174e23aff1c4d4863d6cc2");// 会员号
			request.setMchtOrderNo(mchtOrderNo);
			request.setPhoneNum(phoneNum);// 手机号
			request.setUserName(userName);// 持卡人姓名
			request.setBankCardNo(bankCardNo);// 银行卡
			request.setBankCardType("debit");// debit:借记卡,credit:贷记卡;
			// request.setCvn(RsaUtils.encryptByPublicKey(cvn,
			// RsaUtils.getPublicKey(publicKeyPath)));// cvn 卡背后三位数 信用卡必填
			// request.setExpired(RsaUtils.encryptByPublicKey(expired,
			// RsaUtils.getPublicKey(publicKeyPath)));// 卡有效期 信用卡必填 yymm
			request.setCertificatesNo(certificatesNo);// 身份证号
			request.setCertificatesType("01");// 固定传01
			request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
			ProtocolPayBindCardResponse response = PaymentHelper.bindCard(request);
			log.info(" 易票联创建订单返回消息：" + response.getSmsNo());
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
