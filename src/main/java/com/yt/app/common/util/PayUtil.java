package com.yt.app.common.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
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
import com.yt.app.api.v1.vo.BankcardExtVO;
import com.yt.app.api.v1.vo.BizContentVO;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.PayeeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SyYsOrder;
import com.yt.app.api.v1.vo.SysFcOrder;
import com.yt.app.api.v1.vo.SysFcQuery;
import com.yt.app.api.v1.vo.SysFhOrder;
import com.yt.app.api.v1.vo.SysYSQuery;
import com.yt.app.api.v1.vo.TransListVO;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.api.v1.vo.SysGzOrder;
import com.yt.app.api.v1.vo.SysGzQuery;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysHsQuery;
import com.yt.app.api.v1.vo.SysYSOrder;
import com.yt.app.api.v1.vo.SysRblOrder;
import com.yt.app.api.v1.vo.SysRblQuery;
import com.yt.app.api.v1.vo.SysSnOrder;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.api.v1.vo.SysWdOrder;
import com.yt.app.api.v1.vo.SysWdQuery;
import com.yt.app.api.v1.vo.SysWjOrder;
import com.yt.app.api.v1.vo.SysWjQuery;
import com.yt.app.api.v1.vo.SysXSOrder;
import com.yt.app.api.v1.vo.SysTdQuery;
import com.yt.app.api.v1.vo.SysTdOrder;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayUtil {

	/**
	 * ========================================================================代付
	 * 
	 * @param so
	 * @param key
	 * @return
	 */

	// 天下代付通知回调签名
	public static boolean valMd5TyResultOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id() + "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark=" + so.getRemark() + "&key=" + key;
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
		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid() + "&payaisle=" + ss.getPayaisle() + "&notifyurl=" + ss.getNotifyurl() + "&bankname=" + ss.getBankname() + "&banknum=" + ss.getBanknum()
				+ "&bankowner=" + ss.getBankowner() + "&payamount=" + String.format("%.2f", ss.getPayamount()) + "&key=" + key;
		String sign = MD5Utils.md5(signParams).toUpperCase();
		log.info("盘口代付我方签名:" + signParams + "结果:" + sign + "对方签名:" + ss.getSign());
		if (ss.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口代付通知簽名
	public static String Md5QueryResult(PayResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&outorderid=" + ss.getOutorderid() + "&merchantorderid=" + ss.getMerchantorderid() + "&payamount=" + String.format("%.2f", ss.getPayamount()) + "&key=" + key;
		return MD5Utils.md5(signParams).toUpperCase();
	}

	// 盘口代付通知簽名
	public static String Md5Notify(PayResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&outorderid=" + ss.getOutorderid() + "&merchantorderid=" + ss.getMerchantorderid() + "&payamount=" + String.format("%.2f", ss.getPayamount()) + "&status=" + ss.getStatus() + "&key="
				+ key;
		return MD5Utils.md5(signParams).toUpperCase();
	}

	// 天下代付下单
	public static String SendTxSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + pt.getOrdernum() + "&pay_type=912&pay_amt=" + String.format("%.2f", pt.getAmount()) + "&notify_url="
				+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&return_url=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&bank_code=" + pt.getBankcode()
				+ "&bank_num=" + pt.getAccnumer() + "&bank_owner=" + pt.getAccname() + "&bank_address=" + pt.getBankaddress() + "&remark=payout&key=" + cl.getApikey();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", pt.getOrdernum());
		map.add("user_id", pt.getUserid().toString());
		map.add("user_credit_level", "-" + cl.getAislecode());
		map.add("pay_amt", String.format("%.2f", pt.getAmount()));
		map.add("user_level", "0");
		map.add("pay_type", "912");
		map.add("notify_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("return_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("bank_code", pt.getBankcode());
		map.add("bank_num", pt.getAccnumer());
		map.add("bank_owner", pt.getAccname());
		map.add("bank_address", pt.getBankaddress());
		map.add("user_ip", "127.0.0.1");
		map.add("member_account", pt.getAccname());
		map.add("query_url", cl.getRemark());
		map.add("remark", "payout");

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "/withdraw/create?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		log.info(" 天下代付创建订单：" + data.getTypay_order_id());
		if (data.getPay_message() == 1) {
			return data.getTypay_order_id();
		}
		return null;
	}

	// 天下代付查单
	public static SysTyOrder SendTxSelectOrder(String ordernum, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//

		String signParams = "merchant_id=" + cl.getCode() + "&merchant_order_id=" + ordernum + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", ordernum);
		map.add("remark", "SelectOrder");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/query/withdraw/view?sign=" + MD5Utils.md5(signParams), HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		log.info("天下代付查单：" + data);
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
		//

		String signParams = "merchant_id=" + cl.getCode() + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("MerchantID", cl.getCode());
		map.add("MerchantType", 0);

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
		//
		Long time = System.currentTimeMillis() / 1000;
		Map<String, String> map = new HashMap<String, String>();
		map.put("AccessKey", cl.getCode());
		map.put("OrderNo", pt.getOrdernum());
		map.put("PayChannelId", cl.getAislecode());
		map.put("Amount", String.format("%.2f", pt.getAmount()));
		map.put("CallbackUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
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

		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/submit", httpEntity, String.class);
		String data = sov.getBody();
		SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
		log.info("十年代付创建订单：" + data);
		if (sso.getCode() == 0) {
			return sso.getData().getOrderNo();
		}
		return null;
	}

	// 十年代付查单
	public static SysSnOrder SendSnSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
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

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/queryorder", httpEntity, String.class);
			String data = sov.getBody();
			SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
			log.info("十年代付查单：" + data);
			if (sso.getCode() == 0) {
				return sso;
			}
		} catch (RestClientException e) {
			log.info("十年代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 盛世支付宝代付
	public static String SendSSSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Long time = System.currentTimeMillis() / 1000;
		Map<String, String> map = new HashMap<String, String>();
		map.put("AccessKey", cl.getCode());
		map.put("OrderNo", pt.getOrdernum());
		map.put("PayChannelId", cl.getAislecode());
		map.put("Amount", String.format("%.2f", pt.getAmount()));
		map.put("CallbackUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
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

		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/submit", httpEntity, String.class);
		String data = sov.getBody();
		SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
		log.info("盛世代付创建订单：" + data);
		if (sso.getCode() == 0) {
			return sso.getData().getOrderNo();
		}
		return null;
	}

	// 盛世代付查单
	public static Integer SendSSSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
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

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.postForEntity(cl.getApiip() + "/api/WithdrawalV2/queryorder", httpEntity, String.class);
			String data = sov.getBody();
			SysSnOrder sso = JSONUtil.toBean(data, SysSnOrder.class);
			log.info("盛世代付查订单：" + data);
			if (sso.getCode() == 0) {
				return sso.getData().getStatus();
			}
		} catch (RestClientException e) {
			log.info("盛世代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 易生代付
	public static String SendYSSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("mchId", cl.getCode());
		map.add("mchOrderNo", pt.getOrdernum());
		map.add("remark", "代付" + String.format("%.2f", pt.getAmount()));
		map.add("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
		map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("accountNo", pt.getAccnumer());
		map.add("accountName", pt.getAccname());
		map.add("bankName", pt.getBankname());
		map.add("reqTime", DateTimeUtil.getDateTimeN());

		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.getFirst(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);

		map.add("sign", sign.toUpperCase());

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<String> sov = resttemplate.exchange(cl.getApiip() + "/api/agentpay/apply", HttpMethod.POST, httpEntity, String.class);
		String data = sov.getBody();
		SyYsOrder sso = JSONUtil.toBean(data, SyYsOrder.class);
		log.info("易生代付创建订单：" + data);
		if (sso.getRetCode().equals("SUCCESS")) {
			return sso.getAgentpayOrderId();
		}
		return null;
	}

	// 易生代付查单
	public static Integer SendYSSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("mchId", cl.getCode());
			map.add("mchOrderNo", orderid);
			map.add("reqTime", DateTimeUtil.getDateTimeN());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.exchange(cl.getApiip() + "/api/agentpay/query_order", HttpMethod.POST, httpEntity, String.class);
			String data = sov.getBody();
			SyYsOrder sso = JSONUtil.toBean(data, SyYsOrder.class);
			log.info("易生代付查单：" + data);
			if (sso.getRetCode().equals("SUCCESS")) {
				return sso.getStatus();
			}
		} catch (RestClientException e) {
			log.info("易生代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 旭日支付宝代付
	public static String SendXRSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		String dtime = DateTimeUtil.getDateTime("yyyyMMddHHmmss");
		map.add("mchId", cl.getCode());
		map.add("accountAttr", "0");
		map.add("mchOrderNo", pt.getOrdernum());
		map.add("remark", "代付" + String.format("%.2f", pt.getAmount()));
		map.add("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
		map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("bankCode", pt.getBankcode());
		map.add("accountNo", pt.getAccnumer());
		map.add("accountName", pt.getAccname());
		map.add("bankName", pt.getBankname());
		map.add("reqTime", dtime);

		String signContent = "accountAttr=0&accountName=" + pt.getAccname() + "&accountNo=" + pt.getAccnumer() + "&amount=" + String.format("%.2f", pt.getAmount()).replace(".", "") + "&bankCode=" + pt.getBankcode() + "&bankName=" + pt.getBankname()
				+ "&mchId=" + cl.getCode() + "&mchOrderNo=" + pt.getOrdernum() + "&notifyUrl=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&remark=" + "代付" + String.format("%.2f", pt.getAmount())
				+ "&reqTime=" + dtime + "&keySign=" + cl.getApikey() + "Apm";

		String sign = MD5Utils.md5(signContent);
		map.add("sign", sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/agentpayAPI/apply", httpEntity, JSONObject.class);
		String retCode = sov.getBody().getStr("retCode");
		log.info("旭日代付创建订单：" + sov.getBody());
		if (retCode.equals("SUCCESS")) {
			return sov.getBody().getStr("agentpayOrderId");
		}
		return null;
	}

	// 旭日代付查单
	public static String SendXRSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			String dtime = DateTimeUtil.getDateTime("yyyyMMddHHmmss");
			map.add("mchId", cl.getCode());
			map.add("mchOrderNo", orderid);
			map.add("reqTime", dtime);

			String signContent = "mchId=" + cl.getCode() + "&mchOrderNo=" + orderid + "&reqTime=" + dtime + "&keySign=" + cl.getApikey() + "Apm";

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign);

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/agentpayAPI/queryOrder", httpEntity, JSONObject.class);
			String retCode = sov.getBody().getStr("retCode");
			String status = sov.getBody().getStr("status");
			log.info("旭日代付查订单：" + sov.getBody());
			if (retCode.equals("SUCCESS")) {
				return status;
			}
		} catch (RestClientException e) {
			log.info("旭日代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 守信代付
	public static String SendSXSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Long time = System.currentTimeMillis() / 1000;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", cl.getCode().toString());
		map.put("out_trade_no", pt.getOrdernum().toString());
		map.put("product_id", cl.getAislecode().toString());
		map.put("amount", String.format("%.2f", pt.getAmount()).toString());
		map.put("notify_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.put("time", time.toString());
		JSONObject obj = new JSONObject();
		obj.set("bankName", pt.getBankname());
		obj.set("accountName", pt.getAccname());
		obj.set("accountNumber", pt.getAccnumer());
		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.put("sign", sign);
		map.put("ext", obj);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/payment", httpEntity, JSONObject.class);
		JSONObject data = sov.getBody();
		log.info(" 守信代付创建订单：" + data);
		if (data.getInt("code") == 200) {
			return data.getJSONObject("data").getStr("trade_no");
		}
		return null;
	}

	// 守信代付查单
	public static Integer SendSXSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = System.currentTimeMillis() / 1000;
			map.put("app_id", cl.getCode());
			map.put("out_trade_no", orderid);
			map.put("time", time);
			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/payment/status", httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info("守信代付查单：" + data);
			if (data.getInt("code") == 200) {
				return data.getJSONObject("data").getInt("trade_status");
			}
		} catch (RestClientException e) {
			log.info("守信代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 灵境代付
	public static String SendLJSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Long time = System.currentTimeMillis() / 1000;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("app_id", cl.getCode().toString());
		map.put("out_trade_no", pt.getOrdernum().toString());
		map.put("product_id", cl.getAislecode().toString());
		map.put("amount", String.format("%.2f", pt.getAmount()).toString());
		map.put("notify_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.put("time", time.toString());
		JSONObject obj = new JSONObject();
		obj.set("bankName", pt.getBankname());
		obj.set("accountName", pt.getAccname());
		obj.set("accountNumber", pt.getAccnumer());
		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.put("sign", sign);
		map.put("ext", obj);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/payment", httpEntity, JSONObject.class);
		JSONObject data = sov.getBody();
		log.info(" 灵境代付创建订单：" + data);
		if (data.getInt("code") == 200) {
			return data.getJSONObject("data").getStr("trade_no");
		}
		return null;
	}

	// 灵境代付查单
	public static Integer SendLJSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = System.currentTimeMillis() / 1000;
			map.put("app_id", cl.getCode());
			map.put("out_trade_no", orderid);
			map.put("time", time);
			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/payment/status", httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info("灵境代付查单：" + data);
			if (data.getInt("code") == 200) {
				return data.getJSONObject("data").getInt("trade_status");
			}
		} catch (RestClientException e) {
			log.info("灵境代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// HYT代付
	public static String SendHYTSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("MerchantId", cl.getCode());
		map.add("MerchantUniqueOrderId", pt.getOrdernum());
		map.add("WithdrawTypeId", "0");
		map.add("Amount", String.format("%.2f", pt.getAmount()));
		map.add("NotifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("BankCardNumber", pt.getAccnumer());
		map.add("BankCardRealName", pt.getAccname());
		map.add("BankCardBankName", pt.getBankname());
		map.add("Remark", "");

		String signContent = "Amount=" + String.format("%.2f", pt.getAmount()) + "&BankCardBankName=" + pt.getBankname() + "&BankCardNumber=" + pt.getAccnumer() + "&BankCardRealName=" + pt.getAccname() + "&MerchantId=" + cl.getCode()
				+ "&MerchantUniqueOrderId=" + pt.getOrdernum() + "&NotifyUrl=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&Remark=&WithdrawTypeId=0" + cl.getApikey() + "";

		String sign = MD5Utils.md5(signContent);
		map.add("Sign", sign);

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/InterfaceV9/CreateWithdrawOrder", httpEntity, JSONObject.class);
		String retCode = sov.getBody().getStr("Code");
		log.info("HYT代付创建订单：" + sov.getBody());
		if (retCode.equals("0")) {
			return sov.getBody().getStr("Code");
		}
		return null;
	}

	// HYT代付查单
	public static String SendHYTSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("MerchantId", cl.getCode());
			map.add("MerchantUniqueOrderId", orderid);

			String signContent = "MerchantId=" + cl.getCode() + "&MerchantUniqueOrderId=" + orderid + "" + cl.getApikey() + "";

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign);

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/InterfaceV9/QueryWithdrawOrder", httpEntity, JSONObject.class);
			String retCode = sov.getBody().getStr("Code");
			String status = sov.getBody().getStr("WithdrawOrderStatus");
			log.info("HYT代付查订单：" + sov.getBody());
			if (retCode.equals("0")) {
				return status;
			}
		} catch (RestClientException e) {
			log.info("HYT代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 仙剑代付
	public static String SendXJSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Long time = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mchId", cl.getCode().toString());
		map.put("outTradeNo", pt.getOrdernum().toString());
		map.put("wayCode", cl.getAislecode().toString());
		map.put("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
		map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.put("reqTime", time.toString());
		map.put("payeeName", pt.getAccname());
		map.put("payeeAccount", pt.getAccnumer());
		map.put("payeeBankName", pt.getBankname());
		map.put("clientIp", "127.0.0.1");

		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.put("sign", sign);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/transfer/unifiedorder", httpEntity, JSONObject.class);
		String retCode = sov.getBody().getStr("code");
		log.info("仙剑代付创建订单：" + sov.getBody());
		if (retCode.equals("0")) {
			return sov.getBody().getJSONObject("data").getStr("tradeNo");
		}
		return null;
	}

	// 仙剑代付查单
	public static Integer SendXJSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = System.currentTimeMillis();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time);
			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/transfer/query", httpEntity, JSONObject.class);
			String retCode = sov.getBody().getStr("code");
			Integer status = sov.getBody().getJSONObject("data").getInt("state");
			log.info("仙剑代付查订单：" + sov.getBody());
			if (retCode.equals("0")) {
				return status;
			}
		} catch (RestClientException e) {
			log.info("仙剑代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 青蛙代付
	public static String SendQWSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("mchId", cl.getCode().toString());
		map.add("mchOrderNo", pt.getOrdernum().toString());
		// map.add("passageId", cl.getAislecode().toString());
		map.add("amount", String.format("%.2f", pt.getAmount()).replace(".", ""));
		map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.add("reqTime", DateTimeUtil.getDateTime("yyyyMMddHHmmss"));
		map.add("accountName", pt.getAccname());
		map.add("accountNo", pt.getAccnumer());
		map.add("bankName", pt.getBankname());
		map.add("bankNumber", "其他银行");
		map.add("remark", "payout");

		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key).toString() + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent.replaceAll("\\[", "").replaceAll("\\]", "") + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.add("sign", sign.toUpperCase());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/agentpay/apply", httpEntity, JSONObject.class);
		String retCode = sov.getBody().getStr("retCode");
		log.info("青蛙代付创建订单：" + sov.getBody());
		if (retCode.equals("SUCCESS")) {
			return sov.getBody().getStr("agentpayOrderId");
		}
		return null;
	}

	// 青蛙代付查单
	public static Integer SendQWSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("mchId", cl.getCode());
			map.add("mchOrderNo", orderid);
			map.add("reqTime", DateTimeUtil.getDateTime("yyyyMMddHHmmss"));
			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key).toString() + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent.replaceAll("\\[", "").replaceAll("\\]", "") + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/agentpay/query_order", httpEntity, JSONObject.class);
			String retCode = sov.getBody().getStr("retCode");
			Integer status = sov.getBody().getInt("status");
			log.info("青蛙代付查订单：" + sov.getBody());
			if (retCode.equals("SUCCESS")) {
				return status;
			}
		} catch (RestClientException e) {
			log.info("青蛙代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 8G代付
	public static String Send8GSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Long time = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("merchantNo", cl.getCode().toString());
		map.put("merchantOrderNo", pt.getOrdernum().toString());
		map.put("product", cl.getAislecode().toString());
		map.put("amount", String.format("%.2f", pt.getAmount()));
		map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.put("timestamp", time.toString());
		map.put("receiveName", pt.getAccname());
		map.put("receiveAccount", pt.getAccnumer());
		map.put("bankTag", pt.getBankname());

		TreeMap<String, Object> sortedMap = new TreeMap<>(map);
		String signContent = "";
		for (String key : sortedMap.keySet()) {
			signContent = signContent + key + "=" + map.get(key) + "&";
		}
		signContent = signContent.substring(0, signContent.length() - 1);
		signContent = signContent + "&key=" + cl.getApikey();
		String sign = MD5Utils.md5(signContent);
		map.put("sign", sign);
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/gateway/proxy/init", httpEntity, JSONObject.class);
		String retCode = sov.getBody().getStr("code");
		log.info("8G代付创建订单：" + sov.getBody());
		if (retCode.equals("200")) {
			return sov.getBody().getJSONObject("data").getStr("orderNo");
		}
		return null;
	}

	// 8G代付查单
	public static String Send8GSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("merchantNo", cl.getCode());
			map.put("merchantOrderNo", orderid);
			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/gateway/proxy/query", httpEntity, JSONObject.class);
			String retCode = sov.getBody().getStr("code");
			String status = sov.getBody().getJSONObject("data").getStr("status");
			log.info("8G代付查订单：" + sov.getBody());
			if (retCode.equals("200")) {
				return status;
			}
		} catch (RestClientException e) {
			log.info("8G代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 环宇代付
	public static String SendHYSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("orderid", pt.getOrdernum().toString());
		map.add("money", String.format("%.2f", pt.getAmount()));
		map.add("url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString());
		map.add("name", pt.getAccname());
		map.add("card", pt.getAccnumer());
		map.add("bank", pt.getBankname());

		String signContent = cl.getApikey() + pt.getOrdernum().toString();
		String sign = MD5Utils.md5(signContent);
		map.add("sign", sign.toLowerCase());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/v2/transfer/create", httpEntity, JSONObject.class);
		boolean retCode = sov.getBody().getBool("Result");
		log.info("环宇代付创建订单：" + sov.getBody());
		if (retCode) {
			return "success";
		}
		return null;
	}

	// 环宇代付查单
	public static String SendHYSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("orderid", orderid);
			String signContent = cl.getApikey() + orderid;
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toLowerCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/v2/transfer/query", httpEntity, JSONObject.class);
			boolean retCode = sov.getBody().getBool("Result");
			log.info("环宇代付查订单：" + sov.getBody());
			if (retCode) {
				return sov.getBody().getStr("OrderStatus");
			}
		} catch (RestClientException e) {
			log.info("环宇代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 通银代付对接
	public static String SendTYSubmit(Payout pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			BizContentVO bcv = new BizContentVO();
			bcv.setAccountBookId(cl.getAislecode());
			bcv.setExternalBatchOrderId(pt.getOrdernum());
			List<TransListVO> listpv = new ArrayList<TransListVO>();
			TransListVO tlv = new TransListVO();
			PayeeVO payee = new PayeeVO();
			payee.setIdentity(pt.getAccnumer());
			payee.setName(pt.getAccname());
			tlv.setExternalOrderId(pt.getOrdernum());
			tlv.setType("A");
			if (pt.getType().equals(DictionaryResource.ORDERTYPE_18)) {
				tlv.setType("B");
				BankcardExtVO bev = new BankcardExtVO();
				bev.setAccountType("2");
				payee.setBankcardExt(bev);
			}
			tlv.setTransAmount(String.format("%.2f", pt.getAmount()));
			tlv.setOrderTitle("佣金");
			tlv.setPayeeInfo(payee);
			listpv.add(tlv);
			bcv.setTransList(listpv);
			map.put("mchId", cl.getCode());
			map.put("timestamp", DateTimeUtil.getDateTime());
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("bizContent", JSONUtil.toJsonStr(bcv));
			map.put("signType", "RSA");
			map.put("version", "1.0");

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				sb.append(key).append("=").append(val).append("&");
			}
			String signcontent = sb.toString().substring(0, sb.toString().length() - 1);
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signcontent, cl.getApikey());

			map.put("sign", sign);
			log.info("sign" + "==" + sign);
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/secTrans/batchTransfer/create", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info(" 通银返回消息：" + data);
			if (data.getStr("message").equals("SUCCESS")) {
				return data.getStr("batchOrderId");
			}
		} catch (RestClientException e) {
			log.info(" 通银返回消息：" + e.getMessage());
		}
		return null;
	}

	// 通银代付查单
	public static JSONArray SendTYSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			BizContentVO bcv = new BizContentVO();
			bcv.setExternalBatchOrderId(orderid);
			map.put("mchId", cl.getCode());
			map.put("timestamp", DateTimeUtil.getDateTime());
			map.put("bizContent", JSONUtil.toJsonStr(bcv));
			map.put("signType", "RSA");
			map.put("version", "1.0");

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				sb.append(key).append("=").append(val).append("&");
			}
			String signcontent = sb.toString().substring(0, sb.toString().length() - 1);
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signcontent, cl.getApikey());

			map.put("sign", sign);
			log.info("sign" + "==" + sign);
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/secTrans/batchTransfer/query", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info(" 通银返回消息：" + data);
			if (data.getStr("message").equals("SUCCESS")) {
				return data.getJSONArray("transList");
			}

		} catch (RestClientException e) {
			log.info("通银查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 飞兔代付
	public static String SendFTSubmit(Payout pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//
		Map<String, Object> map = new HashMap<String, Object>();
		String notifyurl = RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip().toString();
		map.put("merchantid", cl.getCode().toString());
		map.put("merchantorderid", pt.getOrdernum().toString());
		map.put("payaisle", cl.getAislecode().toString());
		map.put("payamount", String.format("%.2f", pt.getAmount()));
		map.put("notifyurl", notifyurl);
		map.put("bankowner", pt.getAccname());
		map.put("banknum", pt.getAccnumer());
		map.put("bankname", pt.getBankname());

		String signParams = "merchantid=" + cl.getCode() + "&merchantorderid=" + pt.getOrdernum() + "&payaisle=" + cl.getAislecode() + "&notifyurl=" + notifyurl + "&bankname=" + pt.getBankname() + "&banknum=" + pt.getAccnumer() + "&bankowner="
				+ pt.getAccname() + "&payamount=" + String.format("%.2f", pt.getAmount()) + "&key=" + cl.getApikey();

		String sign = MD5Utils.md5(signParams);
		map.put("sign", sign.toUpperCase());
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		//
		ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/esay/rest/v1/order/submitpayout", httpEntity, JSONObject.class);
		Integer retCode = sov.getBody().getInt("code");
		log.info("飞兔代付创建订单：" + sov.getBody());
		if (retCode == 200) {
			return sov.getBody().getJSONObject("body").getStr("outorderid");
		}
		return null;
	}

	// 飞兔代付查单
	public static Integer SendFTSelectOrder(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("merchantid", cl.getCode());
			map.put("merchantorderid", orderid);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/esay/rest/v1/order/querypayout", httpEntity, JSONObject.class);
			Integer retCode = sov.getBody().getInt("code");
			log.info("飞兔代付创建订单：" + sov.getBody());
			if (retCode == 200) {
				return sov.getBody().getJSONObject("body").getInt("status");
			}
		} catch (RestClientException e) {
			log.info("飞兔代付查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 代付盘口通知
	public static String SendPayoutNotify(String url, PayResultVO ss, String key) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			// 签名
			String signParams = Md5Notify(ss, key);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("merchantid", ss.getMerchantid());
			map.add("outorderid", ss.getOutorderid());
			map.add("merchantorderid", ss.getMerchantorderid());
			map.add("payamount", ss.getPayamount().toString());
			map.add("status", ss.getStatus().toString());
			map.add("remark", ss.getRemark());
			map.add("imgurl", ss.getImgurl());
			map.add("sign", signParams);
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			ResponseEntity<String> sov = resttemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			String data = sov.getBody();
			log.info("商户代付盘口通知" + ss.getOutorderid() + "通知返回:" + data);
			return data;
		} catch (RestClientException e) {
			log.info("请求错误。商户代付盘口通知" + ss.getOutorderid() + "通知返回:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * ================================================================================代收
	 * 
	 * 
	 * @param url
	 * @param ss
	 * @param key
	 * @return
	 */
	// 代收盘口通知
	public static String SendIncomeNotify(String url, QueryQrcodeResultVO ss, String key) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
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
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 代收通知返回签名
	public static String SignMd5QueryResultQrocde(QueryQrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "&pay_amount=" + qr.getPay_amount() + "&pay_code=" + qr.getPay_code() + "&pay_orderid=" + qr.getPay_orderid() + "&key=" + key;
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// KF代收下单
	public static SysHsOrder SendKFSubmit(Income pt, Channel cl) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("memberid", cl.getCode());
		map.add("appid", cl.getApikey());
		map.add("bankcode", pt.getQrcodecode());
		map.add("orderid", pt.getOrdernum());
		map.add("applydate", DateTimeUtil.getDateTime());
		map.add("amount", pt.getRealamount().toString());
		map.add("notify_url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
		map.add("return_url", pt.getBackforwardurl());
		map.add("attach", "goods");
		map.add("sign_type", "RSA2");

		String signContent = "amount=" + pt.getRealamount() + "&appid=" + cl.getApikey() + "&applydate=" + DateTimeUtil.getDateTime() + "&bankcode=" + pt.getQrcodecode() + "&memberid=" + cl.getCode() + "&notify_url="
				+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&orderid=" + pt.getOrdernum() + "&return_url=" + pt.getBackforwardurl();
		String sign = "";
		try {
			sign = sign(signContent, cl.getRemark());
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
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("memberid", cl.getCode());
		map.add("appid", cl.getApikey());
		map.add("orderid", orderid);
		map.add("sign_type", "RSA2");

		String signContent = "appid=" + cl.getApikey() + "&memberid=" + cl.getCode() + "&orderid=" + orderid;
		String sign = "";
		try {
			sign = sign(signContent, cl.getRemark());
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
		//
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("mchid", cl.getCode());
		map.add("sign_type", "RSA2");

		String signContent = "mchid=" + cl.getCode();
		String sign = "";
		try {
			sign = sign(signContent, cl.getRemark());
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

	// 二狗代收对接
	public static SysTdOrder SendEgSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			String datetime = DateTimeUtil.getDateTime();
			map.add("pay_memberid", cl.getCode());
			map.add("pay_bankcode", pt.getQrcodecode());
			map.add("pay_applydate", datetime);
			map.add("pay_orderid", pt.getOrdernum());
			map.add("type", "1");
			map.add("pay_amount", pt.getRealamount().toString());
			map.add("pay_notifyurl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("pay_callbackurl", pt.getBackforwardurl());

			String signContent = "pay_amount=" + pt.getRealamount() + "&pay_applydate=" + datetime + "&pay_bankcode=" + pt.getQrcodecode() + "&pay_callbackurl=" + pt.getBackforwardurl() + "&pay_memberid=" + cl.getCode() + "&pay_notifyurl="
					+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&pay_orderid=" + pt.getOrdernum() + "&key=" + cl.getApikey() + "";

			String sign = MD5Utils.md5(signContent);
			map.add("pay_md5sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysTdOrder> sov = resttemplate.exchange(cl.getApiip() + "/Pay_IndexLink.html", HttpMethod.POST, httpEntity, SysTdOrder.class);
			SysTdOrder data = sov.getBody();
			log.info("二狗返回消息：" + data);
			if (data.getStatus().equals("success")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("二狗返回消息：" + e.getMessage());
		}
		return null;
	}

	// 二狗代收查单
	public static String SendEgQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("pay_memberid", cl.getCode());
			map.add("pay_orderid", orderid);

			String signContent = "pay_memberid=" + cl.getCode() + "&pay_orderid=" + orderid + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("pay_md5sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.exchange(cl.getApiip() + "/Pay_Trade_query.html", HttpMethod.POST, httpEntity, String.class);
			String data = sov.getBody();
			log.info("二狗查单返回消息：" + data);
			SysTdQuery sso = JSONUtil.toBean(data, SysTdQuery.class);
			if (sso.getTrade_state().equals("SUCCESS")) {
				return sso.getTrade_state();
			}
		} catch (RestClientException e) {
			log.info("二狗查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 豌豆代收对接
	public static SysWdOrder SendWdSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("reqTime", time.toString());
			map.add("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");

			String signContent = "amount=" + String.format("%.2f", pt.getRealamount()).replace(".", "") + "&clientIp=127.0.0.1&mchNo=" + cl.getCode() + "&mchOrderNo=" + pt.getOrdernum() + "&notifyUrl="
					+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&productId=" + pt.getQrcodecode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("amount", String.format("%.2f", amount).replace(".", ""));
			map.add("payOrderId", orderid);
			map.add("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", amount).replace(".", "") + "&mchNo=" + cl.getCode() + "&payOrderId=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("reqTime", time.toString());
			String signContent = "mchNo=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getRealamount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")
					+ cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime=" + time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchantId", cl.getCode());
			map.put("orderId", pt.getOrdernum());
			map.put("orderAmount", pt.getRealamount().toString());
			map.put("channelType", pt.getQrcodecode());
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
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
			//
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
			//
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

	// 创世代收对接
	public static SysWjOrder SendWjSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("reqTime", time.toString());
			map.add("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");

			String signContent = "amount=" + String.format("%.2f", pt.getRealamount()).replace(".", "") + "&clientIp=127.0.0.1&mchNo=" + cl.getCode() + "&mchOrderNo=" + pt.getOrdernum() + "&notifyUrl="
					+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&productId=" + pt.getQrcodecode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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

	// 创世代收查单
	public static String SendWjQuerySubmit(String orderid, Double amount, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("amount", String.format("%.2f", amount).replace(".", ""));
			map.add("payOrderId", orderid);
			map.add("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", amount).replace(".", "") + "&mchNo=" + cl.getCode() + "&payOrderId=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchNo", cl.getCode());
			map.add("reqTime", time.toString());
			String signContent = "mchNo=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.add("sign", sign.toUpperCase());

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getRealamount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")
					+ cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime=" + time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("wayCode", pt.getQrcodecode());
			map.put("outTradeNo", pt.getOrdernum());
			map.put("subject", time.toString());
			map.put("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("clientIp", "127.0.0.1");
			map.put("reqTime", time.toString());

			String signContent = "amount=" + String.format("%.2f", pt.getRealamount()).replace(".", "") + "&clientIp=127.0.0.1&mchId=" + cl.getCode() + "&notifyUrl=" + RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain")
					+ cl.getApireusultip() + "&outTradeNo=" + pt.getOrdernum() + "&reqTime=" + time.toString() + "&returnUrl=" + pt.getBackforwardurl() + "&subject=" + time.toString() + "&wayCode=" + pt.getQrcodecode() + "&key=" + cl.getApikey();

			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("outTradeNo", orderid);
			map.put("reqTime", time.toString());

			String signContent = "mchId=" + cl.getCode() + "&outTradeNo=" + orderid + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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
			//
			Map<String, Object> map = new HashMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.put("mchId", cl.getCode());
			map.put("reqTime", time.toString());
			String signContent = "mchId=" + cl.getCode() + "&reqTime=" + time.toString() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);
			map.put("sign", sign);

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

	// 飞黄运通代收对接
	public static SysFhOrder SendFhSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchId", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("currency", "cny");
			map.add("subject", time.toString());
			map.add("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");
			map.add("body", time.toString());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysFhOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/create_order", HttpMethod.POST, httpEntity, SysFhOrder.class);
			SysFhOrder data = sov.getBody();
			log.info("飞黄运通返回消息：" + data);
			if (data.getRetCode().equals("SUCCESS")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("飞黄运通返回消息：" + e.getMessage());
		}
		return null;
	}

	// 飞黄运通代收查单
	public static String SendFhQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("mchId", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("mchOrderNo", orderid);

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysYSQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query_order", HttpMethod.POST, httpEntity, SysYSQuery.class);
			SysYSQuery data = sov.getBody();
			log.info("飞黄运通查单返回消息：" + data);
			if (data.getRetCode().equals("SUCCESS") && data.getStatus().equals("2")) {
				return data.getStatus();
			}
		} catch (RestClientException e) {
			log.info("飞黄运通查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 易生代收对接
	public static SysYSOrder SendYSSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("mchId", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("productId", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("currency", "cny");
			map.add("subject", time.toString());
			map.add("amount", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("body", time.toString());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysYSOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/create_order", HttpMethod.POST, httpEntity, SysYSOrder.class);
			SysYSOrder data = sov.getBody();
			log.info("易生返回消息：" + data);
			if (data.getRetCode().equals("SUCCESS")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("易生返回消息：" + e.getMessage());
		}
		return null;
	}

	// 易生代收查单
	public static String SendYSQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("mchId", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("mchOrderNo", orderid);

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysYSQuery> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query_order", HttpMethod.POST, httpEntity, SysYSQuery.class);
			SysYSQuery data = sov.getBody();
			log.info("易生查单返回消息：" + data);
			if (data.getRetCode().equals("SUCCESS") && data.getStatus().equals("2")) {
				return data.getStatus();
			}
		} catch (RestClientException e) {
			log.info("易生查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 新生代收对接
	public static SysXSOrder SendXSSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			Long time = DateTimeUtil.getNow().getTime();
			map.add("customerCode", cl.getCode());
			map.add("orderCode", pt.getOrdernum());
			map.add("ip", "127.0.0.1");
			map.add("goodsName", time.toString());
			map.add("orderMoney", String.format("%.2f", pt.getRealamount()));
			map.add("uid", pt.getMerchantid());
			map.add("version", "3.0");

			String signContent = "version=3.0&customerCode=" + cl.getCode() + "&orderCode=" + pt.getOrdernum() + "&ip=127.0.0.1&uid=" + pt.getMerchantid() + "&goodsName=" + time + "&orderMoney=" + pt.getRealamount() + "&secret_key=" + cl.getApikey()
					+ "";

			String sign = MD5Utils.md5(signContent.toLowerCase());
			log.info("新生签名：" + signContent + "==" + sign);
			map.add("sign", sign);

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysXSOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, SysXSOrder.class);
			SysXSOrder data = sov.getBody();
			log.info("新生返回消息：" + data);
			if (data.getCode().equals("200")) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("新生返回消息：" + e.getMessage());
		}
		return null;
	}

	// 新生代收查单
	public static String SendXSQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("customerCode", cl.getCode());
			map.add("version", "3.0");
			map.add("orderCode", orderid);

			String signContent = "version=3.0&customerCode=" + cl.getCode() + "&orderCode=" + orderid + "&secret_key=" + cl.getApikey() + "";

			String sign = MD5Utils.md5(signContent.toLowerCase());

			map.add("sign", sign);
			log.info("新生签名：" + signContent + "==" + sign);
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<SysXSOrder> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query", HttpMethod.POST, httpEntity, SysXSOrder.class);
			SysXSOrder data = sov.getBody();
			log.info("新生查单返回消息：" + data);
			if (data.getCode().equals("200")) {
				if (data.getResult().getStatus().equals("2") || data.getResult().getStatus().equals("0"))
					return data.getResult().getStatus();
			}
		} catch (RestClientException e) {
			log.info("新生查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 张三代收对接
	public static JSONObject SendZSSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			Long time = System.currentTimeMillis() / 1000;
			map.put("AccessKey", cl.getCode());
			map.put("PayChannelId", cl.getAislecode());
			map.put("Amount", String.format("%.2f", pt.getRealamount()));
			map.put("OrderNo", pt.getOrdernum());
			map.put("CallbackUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("Timestamp", time.toString());
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);

			String sign = MD5Utils.md5(signContent + "&SecretKey=" + cl.getApikey());
			map.put("Sign", sign);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/PayV2/submit", httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info("张三返回消息：" + sov);
			if (data.getStr("Code").equals("0")) {
				return data.getJSONObject("Data");
			}
		} catch (RestClientException e) {
			log.info("张三返回消息：" + e.getMessage());
		}
		return null;
	}

	// 张三代收查单
	public static JSONObject SendZSQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Long time = System.currentTimeMillis() / 1000;
			Map<String, String> map = new HashMap<String, String>();
			map.put("AccessKey", cl.getCode());
			map.put("OrderNo", orderid);
			map.put("Timestamp", time.toString());
			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.get(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			String sign = MD5Utils.md5(signContent + "&SecretKey=" + cl.getApikey());
			map.put("Sign", sign);

			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.postForEntity(cl.getApiip() + "/api/PayV2/queryorder", httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info("张三查单返回消息：" + data);
			if (data.getStr("Code").equals("0") && data.getJSONObject("Data").getStr("Status").equals("4")) {
				return data.getJSONObject("Data");
			}
		} catch (RestClientException e) {
			log.info("张三查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 通源代收对接
	public static JSONObject SendTongYuanSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mchNo", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("wayCode", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("currency", "cny");
			map.add("subject", DateTimeUtil.getNow().getTime());
			map.add("amount", Integer.parseInt(String.format("%.2f", pt.getRealamount()).replace(".", "")));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");
			map.add("signType", "MD5");
			map.add("divisionMode", 1);
			map.add("version", "1.0");
			map.add("body", DateTimeUtil.getNow().getTime());
			map.add("reqTime", DateTimeUtil.getNow().getTime());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}

			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());
			log.info(signContent + "==" + sign.toUpperCase());
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("通源返回消息：" + data);
			if (data.getInt("orderState") == 1) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("通源返回消息：" + e.getMessage());
		}
		return null;
	}

	// 通源代收查单
	public static JSONObject SendTongYuanQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mchNo", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("mchOrderNo", orderid);
			map.add("signType", "MD5");
			map.add("version", "1.0");
			map.add("reqTime", DateTimeUtil.getNow().getTime());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("通源查单返回消息：" + data);
			if (data.getInt("state") == 2) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("通源查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// oneplus代收对接
	public static JSONObject SendOnePlusSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mchNo", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("wayCode", pt.getQrcodecode());
			map.add("mchOrderNo", pt.getOrdernum());
			map.add("currency", "CNY");
			map.add("subject", DateTimeUtil.getNow().getTime());
			map.add("amount", Integer.parseInt(String.format("%.2f", pt.getRealamount()).replace(".", "")));
			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("clientIp", "127.0.0.1");
			map.add("signType", "MD5");
			map.add("divisionMode", 1);
			map.add("version", "1.0");
			map.add("body", DateTimeUtil.getNow().getTime());
			map.add("reqTime", DateTimeUtil.getNow().getTime());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}

			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());
			log.info(signContent + "==" + sign.toUpperCase());
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("OnePlus返回消息：" + data);
			if (data.getInt("orderState") == 1) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("OnePlus返回消息：" + e.getMessage());
		}
		return null;
	}

	// OnePlus代收查单
	public static JSONObject SendOnePlusQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mchNo", cl.getCode());
			map.add("appId", cl.getRemark());
			map.add("mchOrderNo", orderid);
			map.add("signType", "MD5");
			map.add("version", "1.0");
			map.add("reqTime", DateTimeUtil.getNow().getTime());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}
			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());

			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/query", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("OnePlus查单返回消息：" + data);
			if (data.getInt("state") == 2) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("OnePlus查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 阿力代收对接
	public static JSONObject SendALiSubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", cl.getCode());
			map.put("channel", pt.getQrcodecode());
			map.put("orderId", pt.getOrdernum());
			map.put("returnUrl", pt.getBackforwardurl());
			map.put("nonceStr", "" + DateTimeUtil.getNow().getTime());
			map.put("orderAmt", String.format("%.2f", pt.getRealamount()).replace(".", ""));
			map.put("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.put("ip", "127.0.0.1");
			map.put("desc", "1.0");

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				if (StringUtils.isBlank(val) || key.equalsIgnoreCase("sign")) {
					continue;
				}
				sb.append(key).append("=").append(val).append("&");
			}
			sb.append("key=").append(cl.getApikey());
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(sb.toString(), cl.getPrivatersa());

			map.put("sign", sign.toUpperCase());
			log.info("sign" + "==" + sign.toUpperCase());
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/Api/pay/index", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("阿力返回消息：" + data);
			if (data != null) {
				return data;
			}
		} catch (RestClientException e) {
			log.info("阿力返回消息：" + e.getMessage());
		}
		return null;
	}

	// 阿力代收查单
	public static JSONObject SendALiQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			map.put("merId", cl.getCode());
			map.put("orderId", orderid);
			map.put("nonceStr", "" + DateTimeUtil.getNow().getTime());

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				if (StringUtils.isBlank(val) || key.equalsIgnoreCase("sign")) {
					continue;
				}
				sb.append(key).append("=").append(val).append("&");
			}
			sb.append("key=").append(cl.getApikey());
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(sb.toString(), cl.getPrivatersa());

			map.put("sign", sign.toUpperCase());
			log.info("sign" + "==" + sign.toUpperCase());
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/Api/pay/query", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("阿力查单返回消息：" + data);
			if (data.getInt("status") != null && data.getInt("status") == 1) {
				return data;
			}

		} catch (RestClientException e) {
			log.info("阿力查单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 福铁科技代收对接
	public static JSONObject SendFUTIESubmit(Income pt, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("p0_Cmd", "PreCreate");
			map.add("p1_Account", cl.getCode());
			map.add("p2_Amt", String.format("%.2f", pt.getRealamount()));
			map.add("p3_ProductCode", pt.getQrcodecode());
			map.add("p4_subject", pt.getMerchantname());
			map.add("p7_plat", "2000");
			map.add("p9_Notify_Url", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
			map.add("p11_MerOrderNo", pt.getOrdernum());
			String signContent = "p0_Cmd=PreCreate&p1_Account=" + cl.getCode() + "&p2_Amt=" + String.format("%.2f", pt.getRealamount()) + "&p3_ProductCode=" + pt.getQrcodecode() + "&p4_subject=" + pt.getMerchantname() + "&p7_plat=2000&p9_Notify_Url="
					+ RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip() + "&p11_MerOrderNo=" + pt.getOrdernum() + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("hmac", sign);
			log.info(signContent + "sign:" + sign);
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<String> sov = resttemplate.exchange(cl.getApiip() + "/Api/ApiHandler.ashx", HttpMethod.POST, httpEntity, String.class);
			System.out.println(sov);
//			Integer code = sov.getBody().getInt("Result");
//			JSONObject data = sov.getBody().getJSONObject("data");
//			log.info("福铁科技返回消息：" + data);
//			if (code == 100) {
//				return data;
//			}
		} catch (RestClientException e) {
			log.info("福铁科技返回消息：" + e.getMessage());
		}
		return null;
	}

	// 福铁科技代收查单
	public static JSONObject SendFUTIEQuerySubmit(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
			map.add("mchNo", cl.getCode());
			map.add("appId", cl.getRemark());
//			map.add("wayCode", pt.getQrcodecode());
//			map.add("mchOrderNo", pt.getOrdernum());
//			map.add("currency", "CNY");
//			map.add("subject", DateTimeUtil.getNow().getTime());
//			map.add("amount", Integer.parseInt(String.format("%.2f", pt.getRealamount()).replace(".", "")));
//			map.add("notifyUrl", RedisUtil.get(SystemConstant.CACHE_SYS_CONFIG_PREFIX + "domain") + cl.getApireusultip());
//			map.add("clientIp", "127.0.0.1");
//			map.add("signType", "MD5");
//			map.add("divisionMode", 1);
			map.add("version", "1.0");
			map.add("body", DateTimeUtil.getNow().getTime());
			map.add("reqTime", DateTimeUtil.getNow().getTime());

			TreeMap<String, Object> sortedMap = new TreeMap<>(map);
			String signContent = "";
			for (String key : sortedMap.keySet()) {
				signContent = signContent + key + "=" + map.getFirst(key) + "&";
			}

			signContent = signContent.substring(0, signContent.length() - 1);
			signContent = signContent + "&key=" + cl.getApikey();
			String sign = MD5Utils.md5(signContent);

			map.add("sign", sign.toUpperCase());
			log.info(signContent + "==" + sign.toUpperCase());
			HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/api/pay/unifiedOrder", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody().getJSONObject("data");
			log.info("福铁科技查单返回消息：" + data);
			if (data.getInt("status") != null && data.getInt("status") == 1) {
				return data;
			}

		} catch (RestClientException e) {
			log.info("福铁科技查单返回消息：" + e.getMessage());
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 申請回单/////////////////////////////////////////////////
	// 通银代付申请回单
	public static String SendTYPayCertificate(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			BizContentVO bcv = new BizContentVO();
			bcv.setExternalOrderId(orderid);
			map.put("mchId", cl.getCode());
			map.put("timestamp", DateTimeUtil.getDateTime());
			map.put("bizContent", JSONUtil.toJsonStr(bcv));
			map.put("signType", "RSA");
			map.put("version", "1.0");

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				sb.append(key).append("=").append(val).append("&");
			}
			String signcontent = sb.toString().substring(0, sb.toString().length() - 1);
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signcontent, cl.getApikey());

			map.put("sign", sign);
			log.info("sign" + "==" + sign);
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/secTrans/transfer/receipt/apply", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info(" 通银申请回单返回消息：" + data);
			if (data.getStr("message").equals("SUCCESS")) {
				return data.getStr("message");
			}
		} catch (RestClientException e) {
			log.info("通银申请回单返回消息：" + e.getMessage());
		}
		return null;
	}

	// 通银代付申请回单
	public static String SendTYPayCertificateDownload(String orderid, Channel cl) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//
			Map<String, String> map = new HashMap<String, String>();
			BizContentVO bcv = new BizContentVO();
			bcv.setExternalOrderId(orderid);
			map.put("mchId", cl.getCode());
			map.put("timestamp", DateTimeUtil.getDateTime());
			map.put("bizContent", JSONUtil.toJsonStr(bcv));
			map.put("signType", "RSA");
			map.put("version", "1.0");

			Map<String, String> sortMap = new TreeMap<>(map);
			StringBuffer sb = new StringBuffer();
			Iterator<String> iterator = sortMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String val = sortMap.get(key);
				sb.append(key).append("=").append(val).append("&");
			}
			String signcontent = sb.toString().substring(0, sb.toString().length() - 1);
			// 生成待签名串
			String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signcontent, cl.getApikey());

			map.put("sign", sign);
			log.info("sign" + "==" + sign);
			HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();
			//
			ResponseEntity<JSONObject> sov = resttemplate.exchange(cl.getApiip() + "/secTrans/transfer/receipt/download", HttpMethod.POST, httpEntity, JSONObject.class);
			JSONObject data = sov.getBody();
			log.info(" 通银下载回单返回消息：" + data);
			if (data.getStr("message").equals("SUCCESS")) {
				return data.getStr("downloadUrl");
			}
		} catch (RestClientException e) {
			log.info("通银下载回单返回消息：" + e.getMessage());
		}
		return null;
	}
}
