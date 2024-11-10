package com.yt.app.common.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysFcOrder;
import com.yt.app.api.v1.vo.SysFcQuery;
import com.yt.app.api.v1.vo.SysGzOrder;
import com.yt.app.api.v1.vo.SysGzQuery;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysHsQuery;
import com.yt.app.api.v1.vo.SysRblOrder;
import com.yt.app.api.v1.vo.SysRblQuery;
import com.yt.app.api.v1.vo.SysSnOrder;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.api.v1.vo.SysWdOrder;
import com.yt.app.api.v1.vo.SysWdQuery;
import com.yt.app.api.v1.vo.SysWjOrder;
import com.yt.app.api.v1.vo.SysWjQuery;
import com.yt.app.api.v1.vo.SysYJJQuery;
import com.yt.app.api.v1.vo.SysYjjOrder;

import cn.hutool.json.JSONUtil;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayUtil {

	// 天下代付通知回调签名
	public static boolean valMd5TyResultOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id() + "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark=" + so.getRemark() + "&key=" + key;
		log.info(" 天下代付通知回调签名:" + signParams);
		if (so.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		return false;
	}

	// 天下代付查单返回签名
	public static boolean valMd5TySelectOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id() + "&typay_order_id=" + so.getTypay_order_id() + "&pay_amt=" + String.format("%.2f", so.getPay_amt()) + "&pay_message="
				+ so.getPay_message() + "&remark=SelectOrder&key=" + key;
		log.info(" 天下代付查单回调签名:" + signParams);
		String sign = MD5Utils.md5(signParams);
		log.info("我方签名:" + signParams + "结果:" + sign + "对方签名:" + so.getSign());
		if (so.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口代付下单验证签名
	public static boolean Md5Submit(PaySubmitDTO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid() + "&notifyurl=" + ss.getNotifyurl() + "&bankname=" + ss.getBankname() + "&bankcode=" + ss.getBankcode() + "&banknum=" + ss.getBanknum()
				+ "&bankowner=" + ss.getBankowner() + "&paytype=" + ss.getPaytype() + "&payamt=" + String.format("%.2f", ss.getPayamt()) + "&remark=" + ss.getRemark() + "&key=" + key;
		String sign = MD5Utils.md5(signParams);
		log.info("盘口代付我方签名:" + signParams + "结果:" + sign + "对方签名:" + ss.getSign());
		if (ss.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口代付通知簽名
	public static String Md5Notify(PayResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&payorderid=" + ss.getPayorderid() + "&merchantorderid=" + ss.getMerchantorderid() + "&bankcode=" + ss.getBankcode() + "&payamt=" + String.format("%.2f", ss.getPayamt()) + "&remark="
				+ ss.getRemark() + "&code=" + ss.getCode() + "&key=" + key;
		log.info("盘口代付通知签名:" + signParams);
		return MD5Utils.md5(signParams);
	}

	// 天下代付下单
	public static String SendTxSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + pt.getOrdernum() + "&pay_type=912&pay_amt=" + String.format("%.2f", pt.getAmount()) + "&notify_url=" + cl.getApireusultip() + "&return_url=" + cl.getApireusultip()
				+ "&bank_code=" + pt.getBankcode() + "&bank_num=" + pt.getAccnumer() + "&bank_owner=" + pt.getAccname() + "&bank_address=" + pt.getBankaddress() + "&remark=payout&key=" + cl.getApikey();
		log.info(" 天下代付下单签名：" + signParams);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", pt.getOrdernum());
		map.add("user_id", pt.getUserid().toString());
		map.add("user_credit_level", "-" + cl.getAislecode());
		map.add("pay_amt", String.format("%.2f", pt.getAmount()));
		map.add("user_level", "0");
		map.add("pay_type", "912");
		map.add("notify_url", cl.getApireusultip());
		map.add("return_url", cl.getApireusultip());
		map.add("bank_code", pt.getBankcode());
		map.add("bank_num", pt.getAccnumer());
		map.add("bank_owner", pt.getAccname());
		map.add("bank_address", pt.getBankaddress());
		map.add("user_ip", "127.0.0.1");
		map.add("member_account", pt.getAccname());
		map.add("query_url", cl.getPrivatersa());
		map.add("remark", "payout");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "/withdraw/create?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		log.info(" 天下代付成功返回订单号：" + data.getTypay_order_id() + "返回消息：" + data.getPay_message());
		if (data.getPay_message() == 1) {
			return data.getTypay_order_id();
		}
		return null;
	}

	// 天下代付查单
	public static SysTyOrder SendTxSelectOrder(String ordernum, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + ordernum + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", ordernum);
		map.add("remark", "SelectOrder");
		log.info(" 天下代付查单签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/query/withdraw/view?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		if (valMd5TySelectOrder(data, cl.getApikey())) {
			log.info(data.getTypay_order_id());
			return data;
		} else {
			return null;
		}
	}

	// 天下代付查余额
	public static SysTyBalance SendTxSelectBalance(Channel cl) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + cl.getCode() + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("MerchantID", cl.getCode());
		map.add("MerchantType", 0);
		log.info(" 天下代付查余额签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyBalance> sov = resttemplate.exchange(cl.getApiip() + "/api/query/withdraw/amount?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity, SysTyBalance.class);
		SysTyBalance data = sov.getBody();
		return data;
	}

	// 十年支付宝代付
	public static String SendSnSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		Long time = System.currentTimeMillis() / 1000;
		Map<String, String> map = new HashMap<String, String>();
		map.put("AccessKey", cl.getCode());
		map.put("OrderNo", pt.getOrdernum());
		map.put("PayChannelId", cl.getAislecode());
		map.put("Amount", String.format("%.2f", pt.getAmount()));
		map.put("CallbackUrl", cl.getApireusultip());
		map.put("PayeeNo", pt.getAccnumer());
		map.put("Payee", pt.getAccname());
		map.put("PayeeAddress", pt.getBankname());
		map.put("Timestamp", time.toString());

		TreeMap<String, String> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&SecretKey=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.put("Sign", sign);
		log.info("十年代付下单签名：" + sign + "===" + signContent);

		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/submit", httpEntity, String.class);
		String data = sov.getBody();
		SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
		log.info("十年代付成功返回订单号：" + data);
		if (sso.getCode() == 0) {
			return sso.getData().getOrderNo();
		}
		return null;
	}

	// 公子代收查单
	public static Integer SendSnSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, String> map = new HashMap<String, String>();
			Long time = System.currentTimeMillis() / 1000;
			map.put("AccessKey", cl.getCode());
			map.put("OrderNo", orderid);
			map.put("Timestamp", time.toString());
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&SecretKey=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("Sign", sign);
			log.info("十年查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/queryorder", httpEntity, String.class);
			String data = sov.getBody();
			log.info("十年代付查单返回消息：" + data);
			SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
			log.info("十年代付成功返回订单号：" + data);
			if (sso.getCode() == 0) {
				return sso.getData().getStatus();
			}
		} catch (RestClientException e) {
			log.info("十年代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 代付盘口通知
	public static String SendPayoutNotify(String url, PayResultVO ss, String key) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			// 签名
			String signParams = Md5Notify(ss, key);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("merchantid", ss.getMerchantid());
			map.add("payorderid", ss.getPayorderid());
			map.add("merchantorderid", ss.getMerchantorderid());
			map.add("payamt", ss.getPayamt());
			map.add("bankcode", ss.getBankcode());
			map.add("code", ss.getCode());
			map.add("remark", ss.getRemark());
			map.add("sign", signParams);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			ResponseEntity<String> sov = resttemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			String data = sov.getBody();
			log.info("商户代付盘口通知" + ss.getPayorderid() + "通知返回:" + data);
			return data;
		} catch (RestClientException e) {
			log.info("请求错误。商户代付盘口通知" + ss.getPayorderid() + "通知返回:" + e.getMessage());
		}
		return null;
	}

	// 代收盘口通知
	public static String SendIncomeNotify(String url, QueryQrcodeResultVO ss, String key) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			// 签名
			String signParams = SignMd5QueryResultQrocde(ss, key);
			log.info("商户代收盘口通知签名" + signParams);
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("pay_memberid", ss.getPay_memberid());
			map.add("pay_orderid", ss.getPay_orderid());
			map.add("pay_amount", ss.getPay_amount());
			map.add("pay_code", ss.getPay_code());
			map.add("pay_md5sign", signParams);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			ResponseEntity<String> sov = resttemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			String data = sov.getBody();
			log.info("商户代收盘口通知" + ss.getPay_orderid() + "通知返回:" + data);
			return data;
		} catch (RestClientException e) {
			log.info("请求错误。商户代收盘口通知" + ss.getPay_orderid() + "通知返回:" + e.getMessage());
		}
		return null;
	}

	// 代收下单签名
	public static String SignMd5SubmitQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_amount=" + qs.getPay_amount() + "&pay_applydate=" + qs.getPay_applydate() + "&pay_aislecode=" + qs.getPay_aislecode() + "&pay_callbackurl=" + qs.getPay_callbackurl() + "&pay_memberid=" + qs.getPay_memberid()
				+ "&pay_notifyurl=" + qs.getPay_notifyurl() + "&pay_orderid=" + qs.getPay_orderid() + "&key=" + key;
		log.info("商户代收下单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收查单签名
	public static String SignMd5QueryQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_memberid=" + qs.getPay_memberid() + "&pay_orderid=" + qs.getPay_orderid() + "&key=" + key;
		log.info("商户代收查单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收下单返回签名
	public static String SignMd5ResultQrocde(QrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "&pay_amount=" + qr.getPay_amount() + "&pay_aislecode=" + qr.getPay_aislecode() + "&pay_orderid=" + qr.getPay_orderid() + "&pay_viewurl=" + qr.getPay_viewurl() + "&key=" + key;
		log.info("商户代收下单返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收通知返回签名
	public static String SignMd5QueryResultQrocde(QueryQrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "&pay_amount=" + qr.getPay_amount() + "&pay_code=" + qr.getPay_code() + "&pay_orderid=" + qr.getPay_orderid() + "&key=" + key;
		log.info("商户代收通知返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// KF代收下单
	public static SysHsOrder SendKFSubmit(Income pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("memberid", cl.getCode());
		map.add("appid", cl.getApikey());
		map.add("bankcode", pt.getQrcodecode());
		map.add("orderid", pt.getOrdernum());
		map.add("applydate", DateTimeUtil.getDateTime());
		map.add("amount", pt.getAmount().toString());
		map.add("notify_url", cl.getApireusultip());
		map.add("return_url", pt.getBackforwardurl());
		map.add("attach", "goods");
		map.add("sign_type", "RSA2");

		String signContent = "amount=" + pt.getAmount() + "&appid=" + cl.getApikey() + "&applydate=" + DateTimeUtil.getDateTime() + "&bankcode=" + pt.getQrcodecode() + "&memberid=" + cl.getCode() + "&notify_url=" + cl.getApireusultip() + "&orderid="
				+ pt.getOrdernum() + "&return_url=" + pt.getBackforwardurl();
		String sign = "";
		try {
			sign = sign(signContent, cl.getPrivatersa());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.add("sign", sign);
		log.info("KF下单签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsOrder> sov = resttemplate.exchange(cl.getApiip() + "/pay/order/cashier.do", HttpMethod.POST, httpEntity, SysHsOrder.class);
		SysHsOrder data = sov.getBody();
		log.info("KF返回消息：" + data.getMsg());
		if (data.getStatus().equals("ok")) {
			return data;
		}
		return null;
	}

	// KF代收查单
	public static String SendKFQuerySubmit(String orderid, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("memberid", cl.getCode());
		map.add("appid", cl.getApikey());
		map.add("orderid", orderid);
		map.add("sign_type", "RSA2");

		String signContent = "appid=" + cl.getApikey() + "&memberid=" + cl.getCode() + "&orderid=" + orderid;
		String sign = "";
		try {
			sign = sign(signContent, cl.getPrivatersa());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.add("sign", sign);
		log.info("KF查单签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsQuery> sov = resttemplate.exchange(cl.getApiip() + "/pay/trade/query.do", HttpMethod.POST, httpEntity, SysHsQuery.class);
		SysHsQuery data = sov.getBody();
		log.info("KF查单返回消息：" + data.getTrade_state());
		if (data.getTrade_state().equals("SUCCESS")) {
			return data.getTrade_state();
		}
		return null;
	}

	// KF代收查余额
	public static String SendKFGetBalance(Channel cl) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("mchid", cl.getCode());
		map.add("sign_type", "RSA2");

		String signContent = "mchid=" + cl.getCode();
		String sign = "";
		try {
			sign = sign(signContent, cl.getPrivatersa());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.add("sign", sign);
		log.info("KF余额签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsQuery> sov = resttemplate.exchange(cl.getApiip() + "/Payment/Dfpay/balance.do", HttpMethod.POST, httpEntity, SysHsQuery.class);
		SysHsQuery data = sov.getBody();
		log.info("KF余额返回消息：" + data.getTrade_state());
		if (data.getStatus().equals("success")) {
			return data.getBalance();
		}
		return null;
	}

	public static boolean verifySign(String sign, String data, String publicKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256WITHRSA");
		signature.initVerify(decoderPublicKey(publicKey));
		signature.update(data.getBytes("UTF-8"));
		return signature.verify(Base64.getDecoder().decode(sign));
	}

	/**
	 * 签名
	 *
	 * @param data       业务数据
	 * @param privateKey Base64编码后的私钥
	 * @return 签名后的数据
	 * @throws Exception 异常
	 */
	public static String sign(String data, String privateKey) throws Exception {
		Signature signature = Signature.getInstance("SHA256WITHRSA");
		signature.initSign(decoderPrivateKey(privateKey));
		signature.update(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(signature.sign());
	}

	/**
	 * 根据公钥字符串用Base64解码生成公钥对象
	 *
	 * @param publicKey Base64编码后的公钥字符串
	 * @return 公钥对象
	 * @throws Exception 异常
	 */
	public static PublicKey decoderPublicKey(String publicKey) throws Exception {
		byte[] decode = Base64.getDecoder().decode(publicKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(new X509EncodedKeySpec(decode));
	}

	public static PrivateKey decoderPrivateKey(String privateKey) throws Exception {
		byte[] decode = Base64.getDecoder().decode(privateKey.getBytes());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decode));

	}

	// 雨将军代收对接
	public static SysYjjOrder SendYJJSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("merchant_id", cl.getCode());
			map.add("code", pt.getQrcodecode());
			map.add("order_no", pt.getOrdernum());
			map.add("type", "1");
			map.add("amount", pt.getAmount().toString());
			map.add("notice_url", cl.getApireusultip());
			map.add("return_url", pt.getBackforwardurl());

			String signContent = "amount=" + pt.getAmount() + "&code=" + pt.getQrcodecode() + "&merchant_id=" + cl.getCode() + "&notice_url=" + cl.getApireusultip() + "&order_no=" + pt.getOrdernum() + "&return_url=" + pt.getBackforwardurl()
					+ "&type=1&sign=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("YJJ下单签名：" + sign + "===" + signContent);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysYjjOrder> sov = resttemplate.exchange(cl.getApiip() + "/index/order", HttpMethod.POST, httpEntity, SysYjjOrder.class);
			SysYjjOrder data = sov.getBody();
			log.info("YJJ返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("YJJ返回消息：" + e.getMessage());
		}
		return null;
	}

	// YJJ代收查单
	public static String SendYJJQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("merchant_id", cl.getCode());
			map.add("amount", amount.toString());
			map.add("order_id", orderid);

			String signContent = "amount=" + amount + "&merchant_id=" + cl.getCode() + "&order_id=" + orderid + "&sign=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("YJJ查单签名：" + sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysYJJQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/index/query", HttpMethod.POST, httpEntity, SysYJJQuery.class);
			SysYJJQuery data = sov.getBody();
			log.info("YJJ查单返回消息：" + data);
			if (data.getMsg().equals("ok")) {
				return data.getData().getStatus();
			}
		} catch (RestClientException e) {
			log.info("YJJ查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 豌豆代收对接
	public static SysWdOrder SendWdSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("reqTime", time.toString());
			map.add("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
			map.add("notifyUrl", cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");

			String signContent = "amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&clientIp=127.0.0.1&mchNo=" + cl.getCode() + "&mchOrderNo=" + pt.getOrdernum() + "&notifyUrl=" + cl.getApireusultip() + "&productId="
					+ pt.getQrcodecode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("豌豆下单签名：" + sign.toUpperCase() + "===" + signContent);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWdOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, SysWdOrder.class);
			SysWdOrder data = sov.getBody();
			log.info("豌豆返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("豌豆返回消息：" + e.getMessage());
		}
		return null;
	}

	// 豌豆代收查单
	public static String SendWdQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("amount", String.format("%.2f", amount).replace(".", ""));
			map.add("payOrderId", orderid);
			map.add("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", amount).replace(".", "") + "&mchNo=" + cl.getCode() + "&payOrderId=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("豌豆查单签名：" + sign.toUpperCase() + "===" + signContent);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWdQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query", HttpMethod.POST, httpEntity, SysWdQuery.class);
			SysWdQuery data = sov.getBody();
			log.info("豌豆查单返回消息：" + data);
			if (data.getCode().equals("0") && data.getData().getState().equals("2")) {
				return data.getData().getState();
			}
		} catch (RestClientException e) {
			log.info("豌豆查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 豌豆查询余额
	public static String SendWdGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("reqTime", time.toString());
			String signContent = "mchNo=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("豌豆余额签名：" + sign);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWdQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/mch/queryBalance", HttpMethod.POST, httpEntity, SysWdQuery.class);
			SysWdQuery data = sov.getBody();
			log.info("豌豆余额返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data.getData().getBalance().toString();
			}
		} catch (RestClientException e) {
			log.info("豌豆余额返回消息：" + e.getMessage());
		}
		return null;
	}

	// 日不落代收对接
	public static SysRblOrder SendRblSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
			map.put("notifyUrl", cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime="
					+ time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("日不落下单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysRblOrder> sov = resttemplate.postForEntity(cl.getApiip() + "/api/pay/order", httpEntity, SysRblOrder.class);
			SysRblOrder data = sov.getBody();
			log.info("日不落返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("日不落返回消息：" + e.getMessage());
		}
		return null;
	}

	// 日不落代收查单
	public static String SendRblQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("日不落查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysRblQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/api/pay/query", httpEntity, SysRblQuery.class);
			SysRblQuery data = sov.getBody();
			log.info("日不落查单返回消息：" + data);
			if (data.getCode().equals("0") && data.getData().getState().equals("1")) {
				return data.getData().getState();
			}
		} catch (RestClientException e) {
			log.info("日不落查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 日不落查询余额
	public static String SendRblGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("日不落查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysRblQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/api/mch/balance", httpEntity, SysRblQuery.class);
			SysRblQuery data = sov.getBody();
			log.info("日不落余额返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data.getData().getBalance();
			}
		} catch (RestClientException e) {
			log.info("日不落余额返回消息：" + e.getMessage());
		}
		return null;
	}

	// 公子代收对接
	public static SysGzOrder SendGzSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantId", cl.getCode());
			map.put("orderId", pt.getOrdernum());
			map.put("orderAmount", pt.getAmount().toString());
			map.put("channelType", pt.getQrcodecode());
			map.put("notifyUrl", cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("公子下单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysGzOrder> sov = resttemplate.postForEntity(cl.getApiip() + "/api/newOrder", httpEntity, SysGzOrder.class);
			SysGzOrder data = sov.getBody();
			log.info("公子返回消息：" + data);
			if (data.getCode().equals("200")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("公子返回消息：" + e.getMessage());
		}
		return null;
	}

	// 公子代收查单
	public static String SendGzQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantId", cl.getCode());
			map.put("orderId", orderid);
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("公子查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysGzQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/api/queryOrder", httpEntity, SysGzQuery.class);
			SysGzQuery data = sov.getBody();
			log.info("公子查单返回消息：" + data);
			if (data.getCode().equals("200") && data.getData().getStatus().equals("paid")) {
				return data.getData().getStatus();
			}
		} catch (RestClientException e) {
			log.info("公子查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 公子查询余额
	public static String SendGzGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantId", cl.getCode());
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("公子查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysGzQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/api/queryOrderV2", httpEntity, SysGzQuery.class);
			SysGzQuery data = sov.getBody();
			log.info("公子余额返回消息：" + data);
			if (data.getCode().equals("200")) {
				return "0";
			}
		} catch (RestClientException e) {
			log.info("公子余额返回消息：" + e.getMessage());
		}
		return null;
	}

	// 玩家代收对接
	public static SysWjOrder SendWjSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("reqTime", time.toString());
			map.add("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
			map.add("notifyUrl", cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");

			String signContent = "amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&clientIp=127.0.0.1&mchNo=" + cl.getCode() + "&mchOrderNo=" + pt.getOrdernum() + "&notifyUrl=" + cl.getApireusultip() + "&productId="
					+ pt.getQrcodecode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("玩家下单签名：" + sign.toUpperCase() + "===" + signContent);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWjOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, SysWjOrder.class);
			SysWjOrder data = sov.getBody();
			log.info("玩家返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("玩家返回消息：" + e.getMessage());
		}
		return null;
	}

	// 玩家代收查单
	public static String SendWjQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("amount", String.format("%.2f", amount).replace(".", ""));
			map.add("payOrderId", orderid);
			map.add("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", amount).replace(".", "") + "&mchNo=" + cl.getCode() + "&payOrderId=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("玩家查单签名：" + sign.toUpperCase() + "===" + signContent);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWjQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query", HttpMethod.POST, httpEntity, SysWjQuery.class);
			SysWjQuery data = sov.getBody();
			log.info("玩家查单返回消息：" + data);
			if (data.getCode().equals("0") && data.getData().getState().equals("2")) {
				return data.getData().getState();
			}
		} catch (RestClientException e) {
			log.info("玩家查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 玩家查询余额
	public static String SendWjGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("reqTime", time.toString());
			String signContent = "mchNo=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());
			log.info("玩家余额签名：" + sign);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysWjQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/mch/queryBalance", HttpMethod.POST, httpEntity, SysWjQuery.class);
			SysWjQuery data = sov.getBody();
			log.info("玩家余额返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data.getData().getBalance().toString();
			}
		} catch (RestClientException e) {
			log.info("玩家余额返回消息：" + e.getMessage());
		}
		return null;
	}

	// 翡翠代收对接
	public static SysFcOrder SendFcSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
			map.put("notifyUrl", cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime="
					+ time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("翡翠下单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcOrder> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_SG.html", httpEntity, SysFcOrder.class);
			SysFcOrder data = sov.getBody();
			log.info("翡翠返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("翡翠返回消息：" + e.getMessage());
		}
		return null;
	}

	// 翡翠代收查单
	public static String SendFcQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("翡翠查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_Query.html", httpEntity, SysFcQuery.class);
			SysFcQuery data = sov.getBody();
			log.info("翡翠查单返回消息：" + data);
			if (data.getCode().equals("0") && data.getData().getState().equals("1")) {
				return data.getData().getState();
			}
		} catch (RestClientException e) {
			log.info("翡翠查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 翡翠查询余额
	public static String SendFcGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("翡翠查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_Querybalance.html", httpEntity, SysFcQuery.class);
			SysFcQuery data = sov.getBody();
			log.info("翡翠余额返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data.getData().getBalance();
			}
		} catch (RestClientException e) {
			log.info("翡翠余额返回消息：" + e.getMessage());
		}
		return null;
	}

	// 奥克兰代收对接
	public static SysFcOrder SendAklSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
			map.put("notifyUrl", cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime="
					+ time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("奥克兰下单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcOrder> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_SG.html", httpEntity, SysFcOrder.class);
			SysFcOrder data = sov.getBody();
			log.info("奥克兰返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("奥克兰返回消息：" + e.getMessage());
		}
		return null;
	}

	// 奥克兰代收查单
	public static String SendAklQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("奥克兰查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_Query.html", httpEntity, SysFcQuery.class);
			SysFcQuery data = sov.getBody();
			log.info("奥克兰查单返回消息：" + data);
			if (data.getCode().equals("0") && data.getData().getState().equals("1")) {
				return data.getData().getState();
			}
		} catch (RestClientException e) {
			log.info("奥克兰查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 奥克兰查询余额
	public static String SendAklGetBalance(Channel cl) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);
			log.info("奥克兰查单签名：" + sign + "===" + signContent);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFcQuery> sov = resttemplate.postForEntity(cl.getApiip() + "/Pay_Querybalance.html", httpEntity, SysFcQuery.class);
			SysFcQuery data = sov.getBody();
			log.info("奥克兰余额返回消息：" + data);
			if (data.getCode().equals("0")) {
				return data.getData().getBalance();
			}
		} catch (RestClientException e) {
			log.info("奥克兰余额返回消息：" + e.getMessage());
		}
		return null;
	}
}
