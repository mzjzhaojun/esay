package com.yt.app.common.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;

public class AliPayUtil {

	private static String URL = "https://openapi.alipay.com/gateway.do";

	public static AlipayClient initAliPay(String appid, String private_key, String app_cert_path,
			String alipay_cert_path, String alipay_root_cert_path) throws AlipayApiException {
		AlipayConfig alipayConfig = new AlipayConfig();
		// 设置网关地址
		alipayConfig.setServerUrl(URL);
		// 设置应用APPID
		alipayConfig.setAppId(appid);
		// 设置应用私钥
		alipayConfig.setPrivateKey(private_key);
		// 设置应用公钥证书路径
		alipayConfig.setAppCertPath(app_cert_path);
		// 设置支付宝公钥证书路径
		alipayConfig.setAlipayPublicCertPath(alipay_cert_path);
		// 设置支付宝根证书路径
		alipayConfig.setRootCertPath(alipay_root_cert_path);
		// 设置请求格式，固定值json
		alipayConfig.setFormat("json");
		// 设置字符集
		alipayConfig.setCharset("GBK");
		// 设置签名类型
		alipayConfig.setSignType("RSA2");
		// 构造client
		return new DefaultAlipayClient(alipayConfig);
	}
}
