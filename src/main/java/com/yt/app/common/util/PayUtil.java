package com.yt.app.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.yt.app.api.v1.dbo.SysSubmitDTO;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.common.yt.YtBody;

public class PayUtil {

	private static final Logger logger = LoggerFactory.getLogger(PayUtil.class);

	// 验证菲律宾返回签名
	public static boolean valMd5TyOrder(SysTyOrder so, String key) {
		String signParams = "merchant_id=" + so.getMerchant_id() + "&merchant_order_id=" + so.getMerchant_order_id()
				+ "&typay_order_id=" + so.getTypay_order_id() + "&pay_type=" + so.getPay_type() + "&pay_amt="
				+ String.format("%.2f", so.getPay_amt()) + "&pay_message=" + so.getPay_message() + "&remark="
				+ so.getRemark() + "&key=" + key;

		if (so.getSign().equals(MD5Utils.md5(signParams))) {
			return true;
		}
		logger.info("菲律宾回调签名:" + signParams);
		return false;
	}

	// 盘口下单验证签名
	public static boolean valMd5Submit(SysSubmitDTO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&merchantorderid=" + ss.getMerchantorderid()
				+ "&notifyurl=" + ss.getNotifyurl() + "&bankname=" + ss.getBankname() + "&bankcode=" + ss.getBankcode()
				+ "&banknum=" + ss.getBanknum() + "&bankowner=" + ss.getBankowner() + "&paytype=" + ss.getPaytype()
				+ "&payamt=" + ss.getPayamt() + "&remark=" + ss.getRemark() + "&key=" + key;
		String sign = MD5Utils.md5(signParams);
		logger.info("我方签名:" + signParams + "结果:" + sign + "对方签名:" + ss.getSign());
		if (ss.getSign().equals(sign)) {
			return true;
		}
		return false;
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
		logger.info("菲律宾下单签名：" + signParams);
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
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(cl.getApiip() + "?sign=" + MD5Utils.md5(signParams),
				HttpMethod.POST, httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		//
		logger.info("菲律賓成功返回訂單號：" + data.getTypay_order_id() + "返回消息：" + data.getPay_message());
		return data.getTypay_order_id();
	}

	// 菲律宾查单
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

		System.out.println("菲律宾查单签名：" + signParams);

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysTyOrder> sov = resttemplate.exchange(
				"http://txfat-api.fbnma.com/api/query/withdraw/view?sign=" + MD5Utils.md5(signParams), HttpMethod.POST,
				httpEntity, SysTyOrder.class);
		SysTyOrder data = sov.getBody();
		logger.info(data.getTypay_order_id());
		return data;
	}

	// 盘口通知
	public static YtBody SendNotify(String url, SysResultVO ss, String key) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		// 签名
		String signParams = Md5Result(ss, key);
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
		logger.info("盘口" + ss.getPayorderid() + "通知返回:" + data.getCode());
		return data;
	}

	public static String Md5Result(SysResultVO ss, String key) {
		String signParams = "merchantid=" + ss.getMerchantid() + "&payorderid=" + ss.getPayorderid()
				+ "&merchantorderid=" + ss.getMerchantorderid() + "&bankcode=" + ss.getBankcode() + "&payamt="
				+ ss.getPayamt() + "&remark=" + ss.getRemark() + "&code=" + ss.getCode() + "&key=" + key;
		logger.info("盘口通知签名:" + signParams);
		return MD5Utils.md5(signParams);
	}

}
