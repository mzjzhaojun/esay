package com.yt.app.common.util;

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
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.api.v1.vo.SysTyBalance;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.common.yt.YtBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayUtil {

	// 验证菲律宾通知返回签名
	public static boolean valMd5TyResultOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark="
				+ so.getRemark() + "&key=" + key;
		log.info("菲律宾回调签名:" + signParams);
		if (so.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		return false;
	}

	// 验证菲律宾查查询单返回签名
	public static boolean valMd5TySelectOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_amt=" + String.format("%.2f", so.getPay_amt())
				+ "&pay_message=" + so.getPay_message() + "&remark=SelectOrder&key=" + key;
		log.info("菲律宾查單回调签名:" + signParams);
		String sign = MD5Utils.md5(signParams);
		log.info("我方签名:" + signParams + "结果:" + sign + "对方签名:" + so.getSign());
		if (so.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盘口下单验证签名
	public static boolean Md5Submit(PaySubmitDTO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid()
				+ "&notifyurl=" + ss.getNotifyurl() + "&bankname=" + ss.getBankname() + "&bankcode=" + ss.getBankcode()
				+ "&banknum=" + ss.getBanknum() + "&bankowner=" + ss.getBankowner() + "&paytype=" + ss.getPaytype()
				+ "&payamt=" + String.format("%.2f", ss.getPayamt()) + "&remark=" + ss.getRemark() + "&key=" + key;
		String sign = MD5Utils.md5(signParams);
		log.info("我方签名:" + signParams + "结果:" + sign + "对方签名:" + ss.getSign());
		if (ss.getSign().equals(sign)) {
			return true;
		}
		return false;
	}

	// 盤口通知簽名
	public static String Md5Notify(PayResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&payorderid=" + ss.getPayorderid()
				+ "&merchantorderid=" + ss.getMerchantorderid() + "&bankcode=" + ss.getBankcode() + "&payamt="
				+ String.format("%.2f", ss.getPayamt()) + "&remark=" + ss.getRemark() + "&code=" + ss.getCode()
				+ "&key=" + key;
		log.info("盘口通知签名:" + signParams);
		return MD5Utils.md5(signParams);
	}

	// 菲律宾下单
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
		log.info("菲律宾下单签名：" + signParams);
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
		log.info("菲律賓成功返回訂單號：" + data.getTypay_order_id() + "返回消息：" + data.getPay_message());
		if (data.getPay_message() == 1) {
			return data.getTypay_order_id();
		}
		return null;
	}

	// 菲律宾查单
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
		log.info("菲律宾查单签名：" + signParams);

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

	// 菲律宾查余额
	public static SysTyBalance SendTySelectBalance(Channel cl) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + cl.getCode() + "&key=" + cl.getApikey();

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("MerchantID", cl.getCode());
		map.add("MerchantType", 0);
		log.info("菲律宾查余额签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyBalance> sov = resttemplate.exchange(
				cl.getApiip() + "/api/query/withdraw/amount?sign=" + MD5Utils.md5(signParams), HttpMethod.POST,
				httpEntity, SysTyBalance.class);
		SysTyBalance data = sov.getBody();
		return data;
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
		log.info("盘口" + ss.getPayorderid() + "通知返回:" + data.getCode());
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
		log.info("盘口" + ss.getPay_orderid() + "通知返回:" + data.getCode());
		return data;
	}

	// 拉码下单签名
	public static String SignMd5SubmitQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_amount=" + qs.getPay_amount() + "&pay_applydate=" + qs.getPay_applydate()
				+ "&pay_aislecode=" + qs.getPay_aislecode() + "&pay_callbackurl=" + qs.getPay_callbackurl()
				+ "&pay_memberid=" + qs.getPay_memberid() + "&pay_notifyurl=" + qs.getPay_notifyurl() + "&pay_orderid="
				+ qs.getPay_orderid() + "&key=" + key;
		log.info("拉码下单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 拉码查单签名
	public static String SignMd5QueryQrocde(QrcodeSubmitDTO qs, String key) {
		String stringSignTemp = "pay_memberid=" + qs.getPay_memberid() + "&pay_orderid=" + qs.getPay_orderid() + "&key="
				+ key;
		log.info("拉码查单签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 拉码下单返回签名
	public static String SignMd5ResultQrocde(QrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "pay_amount=" + qr.getPay_amount()
				+ "&pay_aislecode=" + qr.getPay_aislecode() + "&pay_orderid=" + qr.getPay_orderid() + "&pay_viewurl="
				+ qr.getPay_viewurl() + "&key=" + key;
		log.info("拉码下单返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

	// 拉码查单返回签名
	public static String SignMd5QueryResultQrocde(QueryQrcodeResultVO qr, String key) {
		String stringSignTemp = "pay_memberid=" + qr.getPay_memberid() + "pay_amount=" + qr.getPay_amount()
				+ "&pay_code=" + qr.getPay_code() + "&pay_orderid=" + qr.getPay_orderid() + "&key=" + key;
		log.info("拉码查单返回签名:" + stringSignTemp);
		return MD5Utils.md5(stringSignTemp).toUpperCase();
	}

}
