package com.yt.app.common.util;

import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysHsQuery;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.api.v1.vo.SysYJJQuery;
import com.yt.app.api.v1.vo.SysYjjOrder;
import com.yt.app.common.common.yt.YtBody;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayUtil {

	// 老李代付通知回调签名
	public static boolean valMd5TyResultOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark="
				+ so.getRemark() + "&key=" + key;
		log.info("老李代付通知回调签名:" + signParams);
		if (so.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		return false;
	}

	// 老李代付查单返回签名
	public static boolean valMd5TySelectOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_amt=" + String.format("%.2f", so.getPay_amt())
				+ "&pay_message=" + so.getPay_message() + "&remark=SelectOrder&key=" + key;
		log.info("老李代付查单回调签名:" + signParams);
		String sign = MD5Utils.md5(signParams);
		log.info("我方签名:" + signParams + "结果:" + sign + "对方签名:" + so.getSign());
		if (so.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口代付下单验证签名
	public static boolean Md5Submit(PaySubmitDTO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid()
				+ "&notifyurl=" + ss.getNotifyurl() + "&bankname=" + ss.getBankname() + "&bankcode=" + ss.getBankcode()
				+ "&banknum=" + ss.getBanknum() + "&bankowner=" + ss.getBankowner() + "&paytype=" + ss.getPaytype()
				+ "&payamt=" + String.format("%.2f", ss.getPayamt()) + "&remark=" + ss.getRemark() + "&key=" + key;
		String sign = MD5Utils.md5(signParams);
		log.info("盘口代付我方签名:" + signParams + "结果:" + sign + "对方签名:" + ss.getSign());
		if (ss.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口代付通知簽名
	public static String Md5Notify(PayResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&payorderid=" + ss.getPayorderid()
				+ "&merchantorderid=" + ss.getMerchantorderid() + "&bankcode=" + ss.getBankcode() + "&payamt="
				+ String.format("%.2f", ss.getPayamt()) + "&remark=" + ss.getRemark() + "&code=" + ss.getCode()
				+ "&key=" + key;
		log.info("盘口代付通知签名:" + signParams);
		return MD5Utils.md5(signParams);
	}

	// 老李代付下单
	public static String SendTySubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + pt.getOrdernum()
				+ "&pay_type=912&pay_amt=" + String.format("%.2f", pt.getAmount()) + "&notify_url="
				+ cl.getApireusultip() + "&return_url=127.0.0.1&bank_code=" + pt.getBankcode() + "&bank_num="
				+ pt.getAccnumer() + "&bank_owner=" + pt.getAccname() + "&bank_address=" + pt.getBankaddress()
				+ "&remark=" + pt.getRemark() + "&key=" + cl.getApikey();
		log.info("老李代付下单签名：" + signParams);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", pt.getOrdernum());
		map.add("user_id", pt.getId());
		map.add("user_credit_level", "-9_9");
		map.add("pay_amt", String.format("%.2f", pt.getAmount()));
		map.add("user_level", 0);
		map.add("pay_type", 912);
		map.add("notify_url", cl.getApireusultip());
		map.add("return_url", "127.0.0.1");
		map.add("bank_code", pt.getBankcode());
		map.add("bank_num", pt.getAccnumer());
		map.add("bank_owner", pt.getAccname());
		map.add("bank_address", pt.getBankaddress());
		map.add("user_ip", "103.151.116.163");
		map.add("member_account", pt.getAccname());
		map.add("remark", pt.getRemark());

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(
				cl.getApiip() + "/withdraw/create?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity,
				SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		log.info("老李代付成功返回订单号：" + data.getTypay_order_id() + "返回消息：" + data.getPay_message());
		if (data.getPay_message() == 1) {
			return data.getTypay_order_id();
		}
		return null;
	}

	// 老李代付查单
	public static SysTyOrder SendTySelectOrder(String ordernum, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + ordernum + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", ordernum);
		map.add("remark", "SelectOrder");
		log.info("老李代付查单签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(
				cl.getApiip() + "/api/query/withdraw/view?sign=" + MD5Utils.md5(signParams), HttpMethod.POST,
				httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		if (valMd5TySelectOrder(data, cl.getApikey())) {
			log.info(data.getTypay_order_id());
			return data;
		} else {
			return null;
		}
	}

	// 老李代付查余额
	public static SysTyBalance SendTySelectBalance(Channel cl) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + cl.getCode() + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("MerchantID", cl.getCode());
		map.add("MerchantType", 0);
		log.info("老李代付查余额签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyBalance> sov = resttemplate.exchange(
				cl.getApiip() + "/api/query/withdraw/amount?sign=" + MD5Utils.md5(signParams), HttpMethod.POST,
				httpEntity, SysTyBalance.class);
		SysTyBalance data = sov.getBody();
		return data;
	}

	// 宏盛代收查余额
	public static String SendHsSelectBalance(Channel cl) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
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
		log.info("宏盛余额签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsQuery> sov = resttemplate.exchange(cl.getApiip() + "/Payment/Dfpay/balance.do",
				HttpMethod.POST, httpEntity, SysHsQuery.class);
		SysHsQuery data = sov.getBody();
		log.info("宏盛余额返回消息：" + data.getTrade_state());
		if (data.getStatus().equals("success")) {
			return data.getBalance();
		}
		return null;
	}

	// 代付盘口通知
	public static YtBody SendPayoutNotify(String url, PayResultVO ss, String key) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
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
		ResponseEntity<YtBody> sov = resttemplate.exchange(url, HttpMethod.POST, httpEntity, YtBody.class);
		YtBody data = sov.getBody();
		log.info("商户代付盘口通知" + ss.getPayorderid() + "通知返回:" + data.getCode());
		return data;
	}

	// 代收盘口通知
	public static YtBody SendIncomeNotify(String url, QueryQrcodeResultVO ss, String key) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		// 签名
		String signParams = SignMd5QueryResultQrocde(ss, key);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("pay_memberid", ss.getPay_memberid());
		map.add("pay_orderid", ss.getPay_orderid());
		map.add("pay_amount", ss.getPay_amount());
		map.add("pay_code", ss.getPay_code());
		map.add("pay_md5sign", signParams);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<YtBody> sov = resttemplate.exchange(url, HttpMethod.POST, httpEntity, YtBody.class);
		YtBody data = sov.getBody();
		log.info("商户代收盘口通知" + ss.getPay_orderid() + "通知返回:" + data.getCode());
		return data;
	}

	// 代收下单签名
	public static String SignMd5SubmitQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_amount=" + qs.getPay_amount() + "&pay_applydate=" + qs.getPay_applydate()
				+ "&pay_aislecode=" + qs.getPay_aislecode() + "&pay_callbackurl=" + qs.getPay_callbackurl()
				+ "&pay_memberid=" + qs.getPay_memberid() + "&pay_notifyurl=" + qs.getPay_notifyurl() + "&pay_orderid="
				+ qs.getPay_orderid() + "&key=" + key;
		log.info("商户代收下单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收查单签名
	public static String SignMd5QueryQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_memberid=" + qs.getPay_memberid() + "&pay_orderid=" + qs.getPay_orderid() + "&key="
				+ key;
		log.info("商户代收查单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收下单返回签名
	public static String SignMd5ResultQrocde(QrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "pay_amount=" + qr.getPay_amount()
				+ "&pay_aislecode=" + qr.getPay_aislecode() + "&pay_orderid=" + qr.getPay_orderid() + "&pay_viewurl="
				+ qr.getPay_viewurl() + "&key=" + key;
		log.info("商户代收下单返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收通知返回签名
	public static String SignMd5QueryResultQrocde(QueryQrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "pay_amount=" + qr.getPay_amount()
				+ "&pay_code=" + qr.getPay_code() + "&pay_orderid=" + qr.getPay_orderid() + "&key=" + key;
		log.info("商户代收通知返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 宏盛代收下单
	public static SysHsOrder SendHSSubmit(Income pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("memberid", cl.getCode());
		map.add("appid", cl.getApikey());
		map.add("bankcode", pt.getQrcodeaislecode());
		map.add("orderid", pt.getOrdernum());
		map.add("applydate", DateTimeUtil.getDateTime());
		map.add("amount", pt.getAmount().toString());
		map.add("notify_url", cl.getApireusultip());
		map.add("return_url", pt.getBackforwardurl());
		map.add("attach", "goods");
		map.add("sign_type", "RSA2");

		String signContent = "amount=" + pt.getAmount() + "&appid=" + cl.getApikey() + "&applydate="
				+ DateTimeUtil.getDateTime() + "&bankcode=" + pt.getQrcodeaislecode() + "&memberid=" + cl.getCode()
				+ "&notify_url=" + cl.getApireusultip() + "&orderid=" + pt.getOrdernum() + "&return_url="
				+ pt.getBackforwardurl();
		String sign = "";
		try {
			sign = sign(signContent, cl.getPrivatersa());
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.add("sign", sign);
		log.info("宏盛下单签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsOrder> sov = resttemplate.exchange(cl.getApiip() + "/pay/order/cashier.do", HttpMethod.POST,
				httpEntity, SysHsOrder.class);
		SysHsOrder data = sov.getBody();
		log.info("宏盛返回消息：" + data.getMsg());
		if (data.getStatus().equals("ok")) {
			return data;
		}
		return null;
	}

	// 宏盛代收查单
	public static String SendHSQuerySubmit(String orderid, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
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
		log.info("宏盛查单签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysHsQuery> sov = resttemplate.exchange(cl.getApiip() + "/pay/trade/query.do", HttpMethod.POST,
				httpEntity, SysHsQuery.class);
		SysHsQuery data = sov.getBody();
		log.info("宏盛查单返回消息：" + data.getTrade_state());
		if (data.getTrade_state().equals("SUCCESS")) {
			return data.getTrade_state();
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
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("merchant_id", cl.getCode());
		map.add("code", pt.getQrcodeaislecode());
		map.add("order_no", pt.getOrdernum());
		map.add("type", "1");
		map.add("amount", pt.getAmount().toString());
		map.add("notice_url", cl.getApireusultip());
		map.add("return_url", pt.getBackforwardurl());

		String signContent = "amount=" + pt.getAmount() + "&type=1&code=" + pt.getQrcodeaislecode() + "&merchant_id="
				+ cl.getCode() + "&notice_url=" + cl.getApireusultip() + "&order_no=" + pt.getOrdernum()
				+ "&return_url=" + pt.getBackforwardurl() + "&sign=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent.toUpperCase());
		map.add("sign", sign);
		log.info("YJJ下单签名：" + sign + "===" + signContent);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysYjjOrder> sov = resttemplate.exchange(cl.getApiip() + "/index/order", HttpMethod.POST,
				httpEntity, SysYjjOrder.class);
		SysYjjOrder data = sov.getBody();
		log.info("YJJ返回消息：" + data.getMsg());
		if (data.getCode().equals("ok")) {
			return data;
		}
		return null;
	}

	// YJJ代收查单
	public static String SendYJJQuerySubmit(String orderid, Double amount, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("merchant_id", cl.getCode());
		map.add("amount", amount.toString());
		map.add("order_id", orderid);

		String signContent = "amount=" + amount + "&merchant_id=" + cl.getCode() + "&order_id=" + orderid + "&sign="
				+ cl.getApikey();
		String sign = MD5Utils.md5(signContent.toUpperCase());
		map.add("sign", sign);
		log.info("YJJ查单签名：" + sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysYJJQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/index/query", HttpMethod.POST,
				httpEntity, SysYJJQuery.class);
		SysYJJQuery data = sov.getBody();
		log.info("YJJ查单返回消息：" + data.getMsg());
		if (data.getMsg().equals("ok")) {
			return data.getData().getStatus();
		}
		return null;
	}
}
