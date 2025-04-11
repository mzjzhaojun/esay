package com.yt.app.common.util;

import com.alibaba.fastjson.JSONObject;
import com.yt.app.common.util.bo.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RemoteInvoker {
	private final static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	public static <T extends Response> T invoke(Object request, String url, String keyurl, String signno, Class<T> c) throws Exception {
		final String jsonData = request instanceof String ? (String) request : JSONObject.toJSONString(request);
		final String sign = RsaUtils.sign(KeyUtil.getPrimaryKey(), jsonData);// 签名
//		final String sign = RsaUtils.encryptByPublicKey(jsonData, RsaUtils.getPublicKey(keyurl));

		Map<String, String> header = new HashMap<String, String>();
		header.put("x-efps-sign", sign);
		header.put("x-efps-sign-no", signno);
		header.put("x-efps-sign-type", "SHA256withRSA");
		header.put("x-efps-timestamp", df.format(new Date()));
		header.put("Content-Type", "application/json");
		T response = HttpUtil.post(url, jsonData, keyurl, header, c);

		return response;
	}

}
