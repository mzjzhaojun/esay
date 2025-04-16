package com.yt.app.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RemoteInvoker {
	private final static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String invoke(String request, String url) throws Exception {
		final String sign = RsaUtils.sign(KeyUtil.getPrimaryKey(), request);// 签名
		Map<String, String> header = new HashMap<String, String>();
		header.put("x-efps-sign", sign);
		header.put("x-efps-sign-no", "562265003122220003");
		header.put("x-efps-sign-type", "SHA256withRSA");
		header.put("x-efps-timestamp", df.format(new Date()));
		header.put("Content-Type", "application/json");
		String response = HttpUtil.post(url, request, header);

		return response;
	}

}
