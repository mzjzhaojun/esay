package com.yt.app.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.AlipayTradeSettleConfirmModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.AntMerchantExpandIndirectZftDeleteModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.OpenApiRoyaltyDetailInfoPojo;
import com.alipay.api.domain.Participant;
import com.alipay.api.domain.RefundGoodsDetail;
import com.alipay.api.domain.RoyaltyEntity;
import com.alipay.api.domain.SettleConfirmExtendParams;
import com.alipay.api.domain.SettleDetailInfo;
import com.alipay.api.domain.SettleInfo;
import com.alipay.api.domain.SubMerchant;
import com.alipay.api.request.AlipayDataBillEreceiptApplyRequest;
import com.alipay.api.request.AlipayDataBillEreceiptQueryRequest;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.request.AlipayTradeSettleConfirmRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AntMerchantExpandIndirectZftDeleteRequest;
import com.alipay.api.response.AlipayDataBillEreceiptApplyResponse;
import com.alipay.api.response.AlipayDataBillEreceiptQueryResponse;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import com.alipay.api.response.AlipayTradeSettleConfirmResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.alipay.api.response.AntMerchantExpandIndirectZftDeleteResponse;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.common.util.bo.OrderGoods;
import com.yt.app.common.util.bo.OrderInfo;
import com.yt.app.common.util.bo.PaymentQueryRequest;
import com.yt.app.common.util.bo.ProtocolPayRequest;
import com.yt.app.common.util.bo.WithdrawToCardRequest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelfPayUtil {

	private static AlipayConfig getAlipayConfig(Qrcode qrcode) {
		AlipayConfig alipayConfig = new AlipayConfig();
		alipayConfig.setPrivateKey(qrcode.getAppprivatekey());
		alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
		alipayConfig.setAppId(qrcode.getAppid());
		alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
		alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
		alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
		alipayConfig.setAlipayPublicKey(qrcode.getAlipaypublickey());
		return alipayConfig;
	}

	private static AlipayConfig getAlipayConfigCert(Qrcode qrcode) {
		AlipayConfig alipayConfig = new AlipayConfig();
		alipayConfig.setPrivateKey(qrcode.getAppprivatekey());
		alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
		alipayConfig.setAppId(qrcode.getAppid());
		alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
		alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
		alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
		alipayConfig.setAppCertPath(qrcode.getApppublickey());
		alipayConfig.setAlipayPublicCertPath(qrcode.getAlipaypublickey());
		alipayConfig.setRootCertPath(qrcode.getAlipayprovatekey());
		return alipayConfig;
	}

	/**
	 * 支付宝创建订单
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeWapPayResponse AlipayTradeWapPay(Qrcode qrcode, Qrcode sqrcode, String ordernum, Double amount) {

		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));

			AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			request.setNotifyUrl(qrcode.getNotifyurl());
			model.setOutTradeNo(ordernum);
			model.setTotalAmount(amount.toString());
			model.setSubject("Member Payment");
			model.setProductCode("QUICK_WAP_WAY");
			if (sqrcode.getPid() != null) {
				SubMerchant subMerchant = new SubMerchant();
				subMerchant.setMerchantId(sqrcode.getSmid());
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
				extendParams.setSpecifiedSellerName(sqrcode.getName());
				extendParams.setRoyaltyFreeze("false");
				model.setExtendParams(extendParams);
			}
			request.setBizModel(model);
			AlipayTradeWapPayResponse response = alipayClient.pageExecute(request, "GET");
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
	public static AlipayTradeQueryResponse AlipayTradeWapQuery(Qrcode pqrcode, String outno, String ordernum) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(pqrcode));

			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(outno);
			model.setTradeNo(ordernum);
			List<String> queryOptions = new ArrayList<String>();
			queryOptions.add("trade_settle_info");
			model.setQueryOptions(queryOptions);
			request.setBizModel(model);
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝确认结算
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeSettleConfirmResponse AlipayTradeSettleConfirm(Qrcode qrcode, String ordernum, Double amount) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			// 构造请求参数以调用接口
			AlipayTradeSettleConfirmRequest request = new AlipayTradeSettleConfirmRequest();
			AlipayTradeSettleConfirmModel model = new AlipayTradeSettleConfirmModel();

			// 设置确认结算请求流水号
			model.setOutRequestNo(StringUtil.getOrderNum());

			// 设置支付宝交易号
			model.setTradeNo(ordernum);

			// 设置描述结算信息
			SettleInfo settleInfo = new SettleInfo();
			List<SettleDetailInfo> settleDetailInfos = new ArrayList<SettleDetailInfo>();
			SettleDetailInfo settleDetailInfos0 = new SettleDetailInfo();
			settleDetailInfos0.setTransInType("defaultSettle");
			settleDetailInfos0.setAmount(amount.toString());
			settleDetailInfos.add(settleDetailInfos0);
			settleInfo.setSettleDetailInfos(settleDetailInfos);
			model.setSettleInfo(settleInfo);

			// 设置扩展字段信息
			SettleConfirmExtendParams extendParams = new SettleConfirmExtendParams();
			extendParams.setRoyaltyFreeze("false");
			model.setExtendParams(extendParams);

			request.setBizModel(model);

			AlipayTradeSettleConfirmResponse response = alipayClient.execute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝分账
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeOrderSettleResponse AlipayTradeOrderSettle(Qrcode qrcode, String ordernum, Double amount) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			// 构造请求参数以调用接口
			AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
			AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();

			// 设置结算请求流水号
			model.setOutRequestNo(StringUtil.getOrderNum());

			// 设置支付宝订单号
			model.setTradeNo(ordernum);

			// 设置分账明细信息
			List<OpenApiRoyaltyDetailInfoPojo> royaltyParameters = new ArrayList<OpenApiRoyaltyDetailInfoPojo>();
			OpenApiRoyaltyDetailInfoPojo royaltyParameters0 = new OpenApiRoyaltyDetailInfoPojo();
			royaltyParameters0.setRoyaltyType("transfer");
			royaltyParameters0.setTransInType("loginName");
			royaltyParameters0.setTransIn("li1850420@sina.com");
			royaltyParameters0.setAmount(amount.toString());
			royaltyParameters0.setDesc("分账给li1850420@sina.com");
			royaltyParameters.add(royaltyParameters0);
			model.setRoyaltyParameters(royaltyParameters);

			request.setBizModel(model);

			AlipayTradeOrderSettleResponse response = alipayClient.execute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝退款
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeRefundResponse AlipayTradeRefund(Qrcode qrcode, String ordernum, Double amount) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			// 构造请求参数以调用接口
			AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();

			// 设置商户订单号
			model.setOutTradeNo("20150320010101001");

			// 设置支付宝交易号
			model.setTradeNo("2014112611001004680073956707");

			// 设置退款金额
			model.setRefundAmount("200.12");

			// 设置退款原因说明
			model.setRefundReason("正常退款");

			// 设置退款请求号
			model.setOutRequestNo(StringUtil.getOrderNum());

			// 设置退款包含的商品列表信息
			List<RefundGoodsDetail> refundGoodsDetail = new ArrayList<RefundGoodsDetail>();
			RefundGoodsDetail refundGoodsDetail0 = new RefundGoodsDetail();
			refundGoodsDetail0.setOutSkuId("outSku_01");
			refundGoodsDetail0.setOutItemId("outItem_01");
			refundGoodsDetail0.setGoodsId("apple-01");
			refundGoodsDetail0.setRefundAmount("19.50");
			List<String> outCertificateNoList = new ArrayList<String>();
			outCertificateNoList.add("202407013232143241231243243423");
			refundGoodsDetail0.setOutCertificateNoList(outCertificateNoList);
			refundGoodsDetail.add(refundGoodsDetail0);
			model.setRefundGoodsDetail(refundGoodsDetail);

			// 设置退分账明细信息
			List<OpenApiRoyaltyDetailInfoPojo> refundRoyaltyParameters = new ArrayList<OpenApiRoyaltyDetailInfoPojo>();
			OpenApiRoyaltyDetailInfoPojo refundRoyaltyParameters0 = new OpenApiRoyaltyDetailInfoPojo();
			refundRoyaltyParameters0.setAmount("0.1");
			refundRoyaltyParameters0.setTransIn("2088101126708402");
			refundRoyaltyParameters0.setRoyaltyType("transfer");
			refundRoyaltyParameters0.setTransOut("2088101126765726");
			refundRoyaltyParameters0.setTransOutType("userId");
			refundRoyaltyParameters0.setRoyaltyScene("达人佣金");
			refundRoyaltyParameters0.setTransInType("userId");
			refundRoyaltyParameters0.setTransInName("张三");
			refundRoyaltyParameters0.setDesc("分账给2088101126708402");
			refundRoyaltyParameters.add(refundRoyaltyParameters0);
			model.setRefundRoyaltyParameters(refundRoyaltyParameters);

			// 设置查询选项
			List<String> queryOptions = new ArrayList<String>();
			queryOptions.add("refund_detail_item_list");
			model.setQueryOptions(queryOptions);

			// 设置针对账期交易
			model.setRelatedSettleConfirmNo("2024041122001495000530302869");

			request.setBizModel(model);

			AlipayTradeRefundResponse response = alipayClient.execute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝二级商户作废
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AntMerchantExpandIndirectZftDeleteResponse AntMerchantExpandIndirectZftDelete(Qrcode qrcode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			AntMerchantExpandIndirectZftDeleteRequest request = new AntMerchantExpandIndirectZftDeleteRequest();
			AntMerchantExpandIndirectZftDeleteModel model = new AntMerchantExpandIndirectZftDeleteModel();

			// 设置直付通二级商户smid
			model.setSmid(qrcode.getSmid());

			request.setBizModel(model);
			AntMerchantExpandIndirectZftDeleteResponse response = alipayClient.execute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝分账关系绑定
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeRoyaltyRelationBindResponse AlipayTradeRoyaltyRelationBind(Qrcode qrcode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			// 构造请求参数以调用接口
			AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
			AlipayTradeRoyaltyRelationBindModel model = new AlipayTradeRoyaltyRelationBindModel();

			// 设置分账接收方列表
			List<RoyaltyEntity> receiverList = new ArrayList<RoyaltyEntity>();
			RoyaltyEntity receiverList0 = new RoyaltyEntity();
			receiverList0.setLoginName("test@alitest.xyz");
			receiverList0.setName("测试名称");
			receiverList0.setMemo("分账给测试商户");
			receiverList0.setAccountOpenId("093PJtAPYb2UkQ0mXk_X86Z_FaMou-DtIEvERQ8X8yqKaEf");
			receiverList0.setBindLoginName("test@alitest.xyz");
			receiverList0.setType("userId");
			receiverList0.setAccount("2088xxxxx00");
			receiverList.add(receiverList0);
			model.setReceiverList(receiverList);

			// 设置外部请求号
			model.setOutRequestNo(StringUtil.getOrderNum());

			request.setBizModel(model);

			AlipayTradeRoyaltyRelationBindResponse response = alipayClient.execute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝分账关系解除绑定
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayTradeRoyaltyRelationUnbindResponse AlipayTradeRoyaltyRelationUnbind(Qrcode qrcode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
			AlipayTradeRoyaltyRelationUnbindModel model = new AlipayTradeRoyaltyRelationUnbindModel();
			model.setOutRequestNo(StringUtil.getOrderNum());
			List<RoyaltyEntity> receiverList = new ArrayList<RoyaltyEntity>();
			RoyaltyEntity receiverList0 = new RoyaltyEntity();
			receiverList0.setType("userId");
			receiverList0.setAccountOpenId("093PJtAPYb2UkQ0mXk_X86Z_FaMou-DtIEvERQ8X8yqKaEf");
			receiverList0.setBindLoginName("test@alitest.xyz");
			receiverList0.setName("测试名称");
			receiverList0.setLoginName("test@alitest.xyz");
			receiverList0.setAccount("2088xxxxx00");
			receiverList0.setMemo("分账给测试商户");
			receiverList.add(receiverList0);
			model.setReceiverList(receiverList);
			request.setBizModel(model);
			AlipayTradeRoyaltyRelationUnbindResponse response = alipayClient.execute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝账户余额查询
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayFundAccountQueryResponse AlipayFundAccountQuery(Qrcode qrcode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfigCert(qrcode));

			// 构造请求参数以调用接口
			AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
			AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();

			// uid参数未来计划废弃，存量商户可继续使用，新商户请使用openid。请根据应用-开发配置-openid配置选择支持的字段。
			model.setAlipayUserId(qrcode.getRemark());

			// 设置查询的账号类型
			model.setAccountType("ACCTRANS_ACCOUNT");

			request.setBizModel(model);
			AlipayFundAccountQueryResponse response = alipayClient.certificateExecute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝账户余额查询
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayFundTransUniTransferResponse AlipayFundTransUniTransfer(Qrcode qrcode, Qrcodetransferrecord qtc) {
		try {

			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfigCert(qrcode));

			// 构造请求参数以调用接口
			AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
			AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();

			// 设置商家侧唯一订单号
			model.setOutBizNo(qtc.getOutbizno());

			// 设置订单总金额
			model.setTransAmount(qtc.getAmount().toString());

			// 设置描述特定的业务场景
			model.setBizScene("DIRECT_TRANSFER");

			// 设置业务产品码
			model.setProductCode("TRANS_ACCOUNT_NO_PWD");

			// 设置转账业务的标题
			model.setOrderTitle("代发");

			// 设置收款方信息
			Participant payeeInfo = new Participant();
			payeeInfo.setIdentity(qtc.getPayeeid());
			payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
			payeeInfo.setName(qtc.getPayeename());
			model.setPayeeInfo(payeeInfo);

			// 设置业务备注
			model.setRemark("代发");

			// 设置转账业务请求的扩展参数
			model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");

			request.setBizModel(model);
			AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
			log.info(response.getBody());

			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝申请电子回单
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayDataBillEreceiptApplyResponse AlipayDataBillEreceiptApply(Qrcode qrcode, String key) {
		try {

			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfigCert(qrcode));

			AlipayDataBillEreceiptApplyRequest request = new AlipayDataBillEreceiptApplyRequest();
			request.setBizContent("{" + "  \"type\":\"FUND_DETAIL\"," + "  \"key\":\"" + key + "\"}");
			AlipayDataBillEreceiptApplyResponse response = alipayClient.certificateExecute(request);
			log.info(response.getBody());
			if (response.isSuccess()) {
				return response;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 支付宝下载电子回单
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param amount
	 * @return
	 */
	public static AlipayDataBillEreceiptQueryResponse AlipayDataBillEreceiptQuery(Qrcode qrcode, String fileid) {
		try {

			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfigCert(qrcode));

			AlipayDataBillEreceiptQueryRequest request = new AlipayDataBillEreceiptQueryRequest();
			request.setBizContent("{" + "  \"file_id\":\"" + fileid + "\"" + "}");
			AlipayDataBillEreceiptQueryResponse response = alipayClient.certificateExecute(request);
			log.info(response.getBody());
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
	public static String eplpayTradeWapPay(Qrcode qrcode, String memberId, Double amount, String name, String pcardNo, String cardNo, String mobile) {
		try {
			String mchtOrderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			String key = RsaUtils.getPublicKey(qrcode.getApppublickey());
			String userName = RsaUtils.encryptByPublicKey(name, key);
			String certificatesNo = RsaUtils.encryptByPublicKey(pcardNo, key);
			String bankCardNo = RsaUtils.encryptByPublicKey(cardNo, key);
			String phoneNum = RsaUtils.encryptByPublicKey(mobile, key);
			String noncestr = UUID.randomUUID().toString().replaceAll("-", "");
			String param = "{\"version\":\"2.0\",\"mchtOrderNo\":\"" + mchtOrderNo + "\",\"customerCode\":\"" + qrcode.getAppid() + "\",\"memberId\":\"" + memberId + "\",\"userName\":\"" + userName + "\",\"phoneNum\":\"" + phoneNum
					+ "\",\"bankCardNo\":\"" + bankCardNo + "\",\"bankCardType\":\"debit\",\"certificatesType\":\"01\",\"certificatesNo\":\"" + certificatesNo + "\",\"nonceStr\":\"" + noncestr + "\"}";
			log.info(param);
			JSONObject response = PaymentHelper.bindCard(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("smsNo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 协议支付交易
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String eplprotocolPayPre(Qrcode qrcode, String outTradeNo, String memberId, String epfSmsNo, String smscode, Long payAmount) {
		String payCurrency = "CNY"; // 币种，写死
		String attachData = "attachData"; // 备注数据,可空
		String transactionEndTime = ""; // 交易结束时间
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId("test");
		orderInfo.setBusinessType("test");
		orderInfo.addGood(new OrderGoods("订单信息", "1箱", 1));
		ProtocolPayRequest request = new ProtocolPayRequest();
		request.setVersion("3.0");// 必传
		request.setCustomerCode(qrcode.getAppid());
		request.setMemberId(memberId);// 会员号
		request.setPayAmount(payAmount);
		request.setOutTradeNo(outTradeNo);
		request.setPayCurrency(payCurrency);
		request.setAttachData(attachData);
		request.setNeedSplit(false);
		request.setOrderInfo(orderInfo);
		request.setSplitNotifyUrl("");
		request.setSmsNo(epfSmsNo);
		request.setSmsCode(smscode);
		request.setTransactionStartTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		request.setTransactionEndTime(transactionEndTime);
		request.setNotifyUrl(qrcode.getNotifyurl());// 异步通知
		request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
		String param = JSONUtil.toJsonStr(request);
		try {
			JSONObject response = PaymentHelper.protocolPayPre(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("outTradeNo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 交易结果查询
	 */
	public static String eplpaymentQuery(Qrcode qrcode, String outradeno, String noncestr) {
		PaymentQueryRequest request = new PaymentQueryRequest();
		request.setCustomerCode(qrcode.getAppid());// 必填
		request.setOutTradeNo(outradeno);
		request.setNonceStr(noncestr);
		String param = JSONUtil.toJsonStr(request);
		try {
			JSONObject response = PaymentHelper.paymentQuery(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			if (response.getStr("payState").equals("00"))
				return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 确认绑卡
	 * 
	 * @param qrcode
	 * @param ordernum
	 * @param epfsorder
	 * @param smscode
	 * @return
	 */
	public static String eplpaybindCardConfirm(Qrcode qrcode, String memberId, String epfsorder, String smscode) {
		try {
			String noncestr = UUID.randomUUID().toString().replaceAll("-", "");
			String param = "{\"smsNo\":\"" + epfsorder + "\",\"customerCode\":\"" + qrcode.getAppid() + "\",\"memberId\":\"" + memberId + "\",\"smsCode\":\"" + smscode + "\",\"nonceStr\":\"" + noncestr + "\"}";
			JSONObject response = PaymentHelper.bindCardConfirm(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 单笔代付 单笔提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String eplwithdrawalToCard(Qrcode qrcode, String outTradeNo, String name, String cardNo, String bankName, String accountType, Long payAmount) {

		String payCurrency = "CNY";
		String remark = "这是附言";

		String key = RsaUtils.getPublicKey(qrcode.getApppublickey());

		try {
			String bankUserName = RsaUtils.encryptByPublicKey(name, key);
			String bankCardNo = RsaUtils.encryptByPublicKey(cardNo, key);
			WithdrawToCardRequest request = new WithdrawToCardRequest();
			request.setOutTradeNo(outTradeNo);
			request.setCustomerCode(qrcode.getAppid());
			request.setAmount(payAmount);
			request.setBankUserName(bankUserName);
			request.setBankCardNo(bankCardNo);
			request.setBankName(bankName);
			request.setBankAccountType(accountType);// 1：对公，2：对私
			request.setPayCurrency(payCurrency);
			request.setNotifyUrl(qrcode.getPayoutnotifyurl());
			request.setRemark(remark);
			request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.withdrawalToCard(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 单笔代付结果查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String eplwithdrawalToCardQuery(Qrcode qrcode, String outTradeNo) {

		PaymentQueryRequest request = new PaymentQueryRequest();
		request.setCustomerCode(qrcode.getAppid());// 必填
		request.setOutTradeNo(outTradeNo);
		request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.withdrawalToCardQuery(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 余额查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String eplaccountQuery(Qrcode qrcode) {

		Map<String, String> request = new HashMap<String, String>();
		request.put("customerCode", qrcode.getAppid());
		request.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.accountQuery(qrcode.getApirest(), param, qrcode.getAlipayprovatekey(), qrcode.getAlipaypublickey());
			return response.getStr("availableBalance");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
