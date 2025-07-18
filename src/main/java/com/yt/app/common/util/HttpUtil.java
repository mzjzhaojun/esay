package com.yt.app.common.util;

import java.util.Map;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {

	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static JSONObject post(String url, String params, Map<String, String> header) throws Exception {
		log.info("接口地址：" + url);
		try {
			String body = HttpRequest.post(url).addHeaders(header).header("Content-Type", "application/json").body(params).execute().body();
			JSONObject jsonObject = JSONUtil.parseObj(body);
			log.info(" 易票联返回：" + jsonObject.getStr("returnMsg"));
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	/**
	 * 验签
	 * 
	 * @param content 原字符串
	 * @param sign    已签名字符串
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String content, String sign, String publicKey) throws Exception {

		boolean flag = false;
		if (content != null && !content.equals("") && sign != null && !sign.equals("")) {

			boolean result = EplRsaUtils.vertify(EplRsaUtils.getPublicKey(publicKey), content, sign);
			if (result) {
				flag = true;
			}
		}
		return flag;
	}
}
