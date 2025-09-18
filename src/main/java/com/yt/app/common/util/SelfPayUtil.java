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
import java.util.SortedMap;
import java.util.TreeMap;
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
import com.huifu.bspay.sdk.opps.client.BasePayClient;
import com.huifu.bspay.sdk.opps.core.BasePay;
import com.huifu.bspay.sdk.opps.core.config.MerConfig;
import com.huifu.bspay.sdk.opps.core.exception.BasePayException;
import com.huifu.bspay.sdk.opps.core.net.BasePayRequest;
import com.huifu.bspay.sdk.opps.core.request.V2TradePaymentJspayRequest;
import com.huifu.bspay.sdk.opps.core.utils.DateTools;
import com.huifu.bspay.sdk.opps.core.utils.SequenceTools;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.bo.OrderGoods;
import com.yt.app.common.util.bo.OrderInfo;
import com.yt.app.common.util.bo.PaymentQueryRequest;
import com.yt.app.common.util.bo.ProtocolPayRequest;
import com.yt.app.common.util.bo.WithdrawToCardRequest;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelfPayUtil {

	public static boolean getIncomeBySelfPay(Qrcode qd, Qrcode pqd, Income income, Merchant mc, String payamount) {
		boolean flage = true;
		// 直付通手机H5
		if (qd.getCode().equals(DictionaryResource.PRODUCT_ZFTWAP)) {

			AlipayTradeWapPayResponse response = AlipayTradeWapPay(pqd, qd, income.getOrdernum(), income.getRealamount());
			if (response != null) {
				flage = false;
				String pageRedirectionData = response.getBody();
				income.setQrcodeordernum("inqd" + StringUtil.getOrderNum());
				income.setResulturl(pageRedirectionData);
				if (qd.getPid() != null) {
					income.setDynamic(true);
				}
			}
			// 易票联
		} else if (qd.getCode().equals(DictionaryResource.PRODUCT_YPLWAP)) {
			flage = false;
			// 随机数
			income.setAmount(Double.valueOf(payamount));
			if (mc.getClearingtype())
				income.setRealamount(Double.valueOf(StringUtil.getInt(payamount)));
			else
				income.setRealamount(income.getAmount());
			income.setResulturl(income.getQrcode());
			income.setQrcodeordernum("inqd" + StringUtil.getOrderNum());
			// 汇付天下快捷
		} else if (qd.getCode().equals(DictionaryResource.PRODUCT_HUIFUTXWAP)) {
			Map<String, Object> response = quickbuckle(qd, income.getOrdernum(), income.getRealamount(), income.getBackforwardurl());
			if (response != null) {
				flage = false;
				String outradeno = response.get("hf_seq_id").toString();
				String form_url = response.get("form_url").toString();
				income.setResulturl(form_url);
				income.setQrcodeordernum(outradeno);
			}
			// 汇付天下支付宝
		} else if (qd.getCode().equals(DictionaryResource.PRODUCT_HUIFUTXAIPAY)) {
			Map<String, Object> response = quickbucklealipay(qd, income.getOrdernum(), income.getRealamount(), income.getBackforwardurl());
			if (response != null) {
				flage = false;
				String outradeno = response.get("hf_seq_id").toString();
				String form_url = response.get("form_url").toString();
				income.setResulturl(form_url);
				income.setQrcodeordernum(outradeno);
			}
		}
		return flage;
	}

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
			String url = RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + qrcode.getNotifyurl();
			request.setNotifyUrl(url);
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
	public static AlipayTradeQueryResponse AlipayTradeWapQuery(Qrcode pqrcode, String tradeno) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(pqrcode));

			AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setTradeNo(tradeno);
//			List<String> queryOptions = new ArrayList<String>();
//			queryOptions.add("trade_settle_info");
//			model.setQueryOptions(queryOptions);
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
	public static AlipayTradeOrderSettleResponse AlipayTradeOrderSettle(Qrcode qrcode, String ordernum, String transin, Double amount) {
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
			royaltyParameters0.setTransIn(transin);
			royaltyParameters0.setAmount(String.format("%.2f", amount));
			royaltyParameters0.setDesc("百亿补贴-达人佣金");
			royaltyParameters0.setRoyaltyScene("达人佣金");
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
			refundGoodsDetail0.setRefundAmount(String.format("%.2f", amount));
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
	public static AntMerchantExpandIndirectZftDeleteResponse AntMerchantExpandIndirectZftDelete(Qrcode pqrcode, Qrcode qrcode) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(pqrcode));
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
	public static AlipayTradeRoyaltyRelationBindResponse AlipayTradeRoyaltyRelationBind(Qrcode qrcode, String ordernum, String name, String loginName) {
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig(qrcode));
			// 构造请求参数以调用接口
			AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
			AlipayTradeRoyaltyRelationBindModel model = new AlipayTradeRoyaltyRelationBindModel();

			// 设置分账接收方列表
			List<RoyaltyEntity> receiverList = new ArrayList<RoyaltyEntity>();
			RoyaltyEntity receiverList0 = new RoyaltyEntity();
			receiverList0.setLoginName(loginName);
			receiverList0.setName(name);
			receiverList0.setType("loginName");
			receiverList0.setAccount(loginName);
			receiverList0.setBindLoginName(loginName);
			receiverList.add(receiverList0);
			model.setReceiverList(receiverList);

			// 设置外部请求号
			model.setOutRequestNo(ordernum);

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
	 * 支付宝账户转账
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
			model.setOutBizNo(qtc.getOutbizno());
			model.setTransAmount(String.format("%.2f", qtc.getAmount()));
			model.setBizScene("DIRECT_TRANSFER");
			model.setProductCode("TRANS_ACCOUNT_NO_PWD");
			model.setOrderTitle("代发");
			// 设置收款方信息
			Participant payeeInfo = new Participant();
			payeeInfo.setIdentity(qtc.getPayeeid());
			payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
			payeeInfo.setName(qtc.getPayeename());
			model.setPayeeInfo(payeeInfo);
			// 设置业务备注
			model.setRemark("代发");
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
	public static JSONObject eplpayTradeWapPay(Qrcode qrcode, String memberId, Double amount, String name, String pcardNo, String cardNo, String mobile) {
		try {
			String mchtOrderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			String key = EplRsaUtils.getPublicKey(qrcode.getApppublickey());
			String userName = EplRsaUtils.encryptByPublicKey(name.trim(), key);
			String certificatesNo = EplRsaUtils.encryptByPublicKey(pcardNo.trim(), key);
			String bankCardNo = EplRsaUtils.encryptByPublicKey(cardNo.trim(), key);
			String phoneNum = EplRsaUtils.encryptByPublicKey(mobile.trim(), key);
			String noncestr = UUID.randomUUID().toString().replaceAll("-", "");
			String param = "{\"version\":\"2.0\",\"mchtOrderNo\":\"" + mchtOrderNo + "\",\"customerCode\":\"" + qrcode.getAppid() + "\",\"memberId\":\"" + memberId + "\",\"userName\":\"" + userName + "\",\"phoneNum\":\"" + phoneNum
					+ "\",\"bankCardNo\":\"" + bankCardNo + "\",\"bankCardType\":\"debit\",\"certificatesType\":\"01\",\"certificatesNo\":\"" + certificatesNo + "\",\"nonceStr\":\"" + noncestr + "\"}";
			log.info(param);
			JSONObject response = PaymentHelper.bindCard(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联协议支付交易
	 * 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject eplprotocolPayPre(Qrcode qrcode, String outTradeNo, String memberId, String epfSmsNo, String smscode, Long payAmount) {
		log.info("smscode" + smscode);
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
		request.setSmsCode(smscode.trim());
		request.setTransactionStartTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		request.setTransactionEndTime(transactionEndTime);
		request.setNotifyUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + qrcode.getNotifyurl());// 异步通知
		request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
		String param = JSONUtil.toJsonStr(request);
		try {
			JSONObject response = PaymentHelper.protocolPayPre(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联交易结果查询
	 */
	public static String eplpaymentQuery(Qrcode qrcode, String outradeno, String noncestr) {
		PaymentQueryRequest request = new PaymentQueryRequest();
		request.setCustomerCode(qrcode.getAppid());// 必填
		request.setOutTradeNo(outradeno);
		request.setNonceStr(noncestr);
		String param = JSONUtil.toJsonStr(request);
		try {
			JSONObject response = PaymentHelper.paymentQuery(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			if (response.getStr("payState").equals("00"))
				return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联确认绑卡
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
			JSONObject response = PaymentHelper.bindCardConfirm(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response.getStr("returnCode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联单笔代付 单笔提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject eplwithdrawalToCard(Qrcode qrcode, String outTradeNo, String name, String cardNo, String bankName, Long payAmount) {

		String payCurrency = "CNY";
		String remark = "附言";

		String key = EplRsaUtils.getPublicKey(qrcode.getApppublickey());
		if (generalRequest(cardNo)) {
			try {
				String bankUserName = EplRsaUtils.encryptByPublicKey(name, key);
				String bankCardNo = EplRsaUtils.encryptByPublicKey(cardNo, key);
				WithdrawToCardRequest request = new WithdrawToCardRequest();
				request.setOutTradeNo(outTradeNo);
				request.setCustomerCode(qrcode.getAppid());
				request.setAmount(payAmount);
				request.setBankUserName(bankUserName);
				request.setBankCardNo(bankCardNo);
				request.setBankName(bankName);
				request.setBankAccountType("2");// 1：对公，2：对私
				request.setPayCurrency(payCurrency);
				request.setNotifyUrl(RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + qrcode.getPayoutnotifyurl());
				request.setRemark(remark);
				request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
				String param = JSONUtil.toJsonStr(request);
				log.info(param);
				JSONObject response = PaymentHelper.withdrawalToCard(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
				return response;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 易票联单笔代付结果查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject eplwithdrawalToCardQuery(Qrcode qrcode, String outTradeNo) {

		PaymentQueryRequest request = new PaymentQueryRequest();
		request.setCustomerCode(qrcode.getAppid());// 必填
		request.setOutTradeNo(outTradeNo);
		request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.withdrawalToCardQuery(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联单笔代付凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject eplwithdrawalCertification(Qrcode qrcode, String outTradeNo) {

		PaymentQueryRequest request = new PaymentQueryRequest();
		request.setCustomerCode(qrcode.getAppid());// 必填
		request.setOutTradeNo(outTradeNo);
		request.setNonceStr(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.withdrawalCertification(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 易票联余额查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public static JSONObject eplaccountQuery(Qrcode qrcode) {

		Map<String, String> request = new HashMap<String, String>();
		request.put("customerCode", qrcode.getAppid());
		request.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String param = JSONUtil.toJsonStr(request);
			JSONObject response = PaymentHelper.accountQuery(qrcode.getApirest(), param, qrcode.getSmid(), qrcode.getAppprivatekey());
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 汇付下单快捷
	 * 
	 * @param qrcode
	 * @return
	 */
	public static Map<String, Object> quickbuckle(Qrcode qrcode, String ordernum, Double amount, String backurl) {

		// 1. 数据初始化，填入对应的商户配置
		MerConfig merConfig = new MerConfig();
		merConfig.setProcutId(qrcode.getSmid());
		merConfig.setSysId(qrcode.getAppid());
		merConfig.setRsaPrivateKey(qrcode.getAppprivatekey());
		merConfig.setRsaPublicKey(qrcode.getApppublickey());
		try {
			BasePay.initWithMerConfig(merConfig);
			String date = DateUtil.format(DateUtil.YMD, new Date());

			// 2.组装请求参数
			Map<String, Object> paramsInfo = new HashMap<>();
			// 业务请求流水号
			paramsInfo.put("req_seq_id", ordernum);
			// 请求日期
			paramsInfo.put("req_date", date);
			// 商户号
			paramsInfo.put("huifu_id", qrcode.getAppid());
			// 订单金额
			paramsInfo.put("trans_amt", String.format("%.2f", amount));
			// 订单类型
			paramsInfo.put("request_type", "M");
			// 银行扩展信息
			paramsInfo.put("extend_pay_data", "{\"goods_short_name\":\"01\",\"biz_tp\":\"123451\",\"gw_chnnl_tp\":\"02\"}");
			// 设备信息
			paramsInfo.put("terminal_device_data", "{\"device_type\":\"1\",\"device_ip\":\"127.0.0.1\"}");
			// 安全信息
			paramsInfo.put("risk_check_data", "{\"ip_addr\":\"127.0.0.1\"}");
			// 异步通知地址
			paramsInfo.put("notify_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + qrcode.getNotifyurl());
			// 商品描述
			paramsInfo.put("goods_desc", "快捷支付接口");
			// 备注
			paramsInfo.put("remark", "remark快捷支付接口");
			// 页面跳转地址
			paramsInfo.put("front_url", backurl);
			// 3. 发起API调用
			Map<String, Object> response = BasePayRequest.requestBasePay("v2/trade/onlinepayment/quickpay/frontpay", paramsInfo, null, false);
			return response;
		} catch (BasePayException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 汇付快捷交易结果查询
	 */
	public static String huifupaymentQuery(Qrcode qrcode, String outradeno) {
		MerConfig merConfig = new MerConfig();
		merConfig.setProcutId(qrcode.getSmid());
		merConfig.setSysId(qrcode.getAppid());
		merConfig.setRsaPrivateKey(qrcode.getAppprivatekey());
		merConfig.setRsaPublicKey(qrcode.getApppublickey());
		try {
			BasePay.initWithMerConfig(merConfig);
			String date = DateUtil.format(DateUtil.YMD, new Date());

			// 2.组装请求参数
			Map<String, Object> paramsInfo = new HashMap<>();
			// 商户号
			paramsInfo.put("huifu_id", qrcode.getAppid());
			// 原交易请求日期
			paramsInfo.put("org_req_date", date);
			paramsInfo.put("org_req_seq_id", outradeno);
			// 原交易支付类型QUICK_PAY：快捷支付、快捷充值(查询快捷交易必填)&lt;br/&gt;ONLINE_PAY：网银支付、网银充值&lt;br/&gt;WAP_PAY：手机WAP支付&lt;br/&gt;UNION_PAY：银联APP统一支付&lt;br/&gt;QUICK_PAY_APPLY：银行卡分期申请&lt;br/&gt;QUICK_PAY_CONFIRM：银行卡分期确认&lt;br/&gt;TRANSFER_ACCT：网银转账&lt;br/&gt;&lt;font
			paramsInfo.put("pay_type", "QUICK_PAY");
			// 3. 发起API调用
			Map<String, Object> response = BasePayRequest.requestBasePay("v2/trade/onlinepayment/query", paramsInfo, null, false);
			if (response.get("trans_stat").toString().equals("S")) {
				return response.get("trans_stat").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 汇付下单支付宝
	 * 
	 * @param qrcode
	 * @return
	 */
	public static Map<String, Object> quickbucklealipay(Qrcode qrcode, String ordernum, Double amount, String backurl) {

		// 1. 数据初始化，填入对应的商户配置
		MerConfig merConfig = new MerConfig();
		merConfig.setProcutId(qrcode.getSmid());
		merConfig.setSysId(qrcode.getAppid());
		merConfig.setRsaPrivateKey(qrcode.getAppprivatekey());
		merConfig.setRsaPublicKey(qrcode.getApppublickey());
		try {
			BasePay.initWithMerConfig(merConfig);

			// 2.组装请求参数
			V2TradePaymentJspayRequest request = new V2TradePaymentJspayRequest();
			// 请求日期
			request.setReqDate(DateTools.getCurrentDateYYYYMMDD());
			// 请求流水号
			request.setReqSeqId(SequenceTools.getReqSeqId32());
			// 商户号
			request.setHuifuId(qrcode.getAppid());
			// 交易类型
			request.setTradeType("A_NATIVE");
			// 交易金额
			request.setTransAmt("0.10");
			// 商品描述
			request.setGoodsDesc("商品");

			// 3. 发起API调用
			Map<String, Object> response = BasePayClient.request(request, false);
			System.out.println("返回数据:" + response);

			return response;
		} catch (BasePayException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 汇付支付宝交易结果查询
	 */
	public static String huifualipaymentQuery(Qrcode qrcode, String outradeno) {
		MerConfig merConfig = new MerConfig();
		merConfig.setProcutId(qrcode.getSmid());
		merConfig.setSysId(qrcode.getAppid());
		merConfig.setRsaPrivateKey(qrcode.getAppprivatekey());
		merConfig.setRsaPublicKey(qrcode.getApppublickey());
		try {
			BasePay.initWithMerConfig(merConfig);
			String date = DateUtil.format(DateUtil.YMD, new Date());

			// 2.组装请求参数
			Map<String, Object> paramsInfo = new HashMap<>();
			// 商户号
			paramsInfo.put("huifu_id", qrcode.getAppid());
			// 原交易请求日期
			paramsInfo.put("org_req_date", date);
			paramsInfo.put("org_req_seq_id", outradeno);
			// 3. 发起API调用
			Map<String, Object> response = BasePayRequest.requestBasePay("v3/trade/payment/scanpay/query", paramsInfo, null, false);
			if (response.get("trans_stat").toString().equals("S")) {
				return response.get("trans_stat").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// appId(从对接运营人员获取)
	public static final String APP_ID = "41722c4337bf461f95fed7801dfb8553";
	public static final String APP_SECRET = "x09OEujyCCypsVxA1ArBC+ZMY2ydOii5gRlRk4wAaj4=";
	// 请求URL地址
	public static final String URL = "https://api-safefund.yuanshouyin.com";

	public static boolean generalRequest(String accnumber) {

		SortedMap<String, Object> map = new TreeMap<>();
		map.put("name", "XJT数据");
		map.put("identity", accnumber);

		// 时间戳
		Long timestamp = System.currentTimeMillis();
		// 准备参数，请求头参数使用TreeMap自动排序字段；便于验签
		SortedMap<String, String> headers = new TreeMap<>();
		headers.put("appId", APP_ID);
		headers.put("nonce", StringUtil.getOrderNum());
		headers.put("timestamp", String.valueOf(timestamp));
		// 请求参数格式化为json字符串，用于加签验证
		String paramJsonStr = JSONUtil.toJsonStr(map);
		String param = paramJsonStr + MapUtil.join(headers, "&", "=") + APP_SECRET;
		// 采用sha256Hex签名
		String sign = DigestUtil.sha256Hex(param);
		// 拼接字符串,签名开头保留，${} 全替换为header参数
		headers.put("sign", sign);
		// 固定值，加签后赋值
		headers.put("tenant-id", "1");
		// 执行请求调用
//        String body = HttpUtils.post(URL + "/app-api/fms/risk-exterior-query/api/query", headers, map);
//        JSONArray data=  JSONUtil.parseObj(body).getJSONObject("data").getJSONArray("data");
//        if(data.size()>1) {
//        	log.info("test card fait"+accnumber);
//        	return false;
//        }
		log.info("test card succcess" + accnumber);
		return true;
	}
}
