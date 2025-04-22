package com.yt.app.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;

public class RemoteInvoker {
	private final static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public static JSONObject invoke(String request, String url, String signNo, String privatekey) throws Exception {
		final String sign = EplRsaUtils.sign(privatekey, request);// 签名
		Map<String, String> header = new HashMap<String, String>();
		header.put("x-efps-sign", sign);
		header.put("x-efps-sign-no", signNo);
		header.put("x-efps-sign-type", "SHA256withRSA");
		header.put("x-efps-timestamp", df.format(new Date()));
		header.put("Content-Type", "application/json");
		JSONObject response = HttpUtil.post(url, request, header);
		Assert.notNull(response, "易票联查询出错!");
		return response;
	}

}
