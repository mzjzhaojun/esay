package com.yt.app.common.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.SysResult;
import com.yt.app.api.v1.vo.SysSubmit;
import com.yt.app.api.v1.vo.SysTyOrder;

public class PayUtil {

	public static boolean valMd5TyOrder(SysTyOrder so, String key) {

		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark="
				+ so.getRemark() + "&key=" + key;

		if (so.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		return false;
	}

	public static boolean valMd5Submit(SysSubmit ss, String key) {

		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid()
				+ "&notify_url=" + ss.getNotifyurl() + "&bank_code=" + ss.getBankcode() + "&bank_num=" + ss.getBanknum()
				+ "&bank_owner=" + ss.getBankowner() + "&paytype=" + ss.getPaytype() + "&payamt=" + ss.getPayamt()
				+ "&remark=" + ss.getRemark() + "&key=" + key;
		System.out.println(MD5Utils.md5(signParams));
		if (ss.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		return false;
	}

	public static String Md5Result(SysResult ss, String key) {

		String signParams = "merchantid=" + ss.getMerchantid() + "&payorderid=" + ss.getPayorderid()
				+ "&merchantorderid=" + ss.getMerchantorderid() + "&bank_code=" + ss.getBankcode() + "&paytype="
				+ ss.getPaytype() + "&payamt=" + ss.getPayamt() + "&remark=" + ss.getRemark() + "&key=" + key;

		return MD5Utils.md5(signParams);
	}

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
		System.out.println(signParams);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", cl.getCode());
		map.add("merchant_order_id", pt.getOrdernum());
		map.add("user_id", pt.getAccnumer());
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
		map.add("user_ip", "103.151.116.235");
		map.add("member_account", pt.getAccname());
		map.add("remark", pt.getRemark());

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "?sign=" + MD5Utils.md5(signParams),
				HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		System.out.println(data.getTypay_order_id());
		return data.getTypay_order_id();
	}

	public static SysTyOrder SendTySelect(String ordernum, Integer channelcode, String key) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		String signParams = "merchant_id=" + channelcode + "&merchant_order_id=" + ordernum + "&key=" + key;

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("merchant_id", channelcode);
		map.add("merchant_order_id", ordernum);
		map.add("remark", "select");

		System.out.println("查詢签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(
				"http://txfat-api.fbnma.com/api/query/withdraw/view?sign=" + MD5Utils.md5(signParams), HttpMethod.POST,
				httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		System.out.println(data.getTypay_order_id());
		return data;
	}

}
