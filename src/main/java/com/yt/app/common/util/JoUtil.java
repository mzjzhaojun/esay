package com.yt.app.common.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import cn.hutool.json.JSONObject;

public class JoUtil {
	// 初始化需要用到的参数
	private static final String CLIENT_TYPE = "1";
	private static final String CLIENT_VERSION = "1.0.0";
	private static final String API_SECRET = "af7ce51cac2c38466ad835480ba796b8";
	private static final String API_KEY = "jo_pay_JYhmAlbDORI2J6HWOktwdr70B";
	private static final String STRING_KEY = "open_api_vf12kwQ75v7xd333${clientType}${clientVersion}${apiKey}${apiSecret}${timestamp}";
	private static final String URL = "https://pay.new.jieoukeji.cn/jo-pay-open-api";
	private static final String PATH = "/v1/bank/info";// 示例接口：获取银行卡信息
	private static final Pattern VALUE_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");

	public static void main(String[] args) {
		testsend();
	}

	public static void testsend() {
		long timestamp = System.currentTimeMillis();
		Map<String, String> headers = new HashMap<>(6);
		headers.put("client-type", CLIENT_TYPE);
		headers.put("client-version", CLIENT_VERSION);
		headers.put("api-secret", API_SECRET);
		headers.put("api-key", API_KEY);
		headers.put("access-timestamp", String.valueOf(timestamp));
		headers.put("Content-Type", "application/json");

		// 拼接字符串,签名开头保留，${} 全替换为header参数
		headers.put("access-sign", getSign(STRING_KEY, timestamp));
		// 开始请求API接口
		Map<String, String> param = new HashMap<>();
		param.put("cardNo", "123546345674573457");
		try {
			String results = HttpUtils.get(URL + PATH, headers, param);
			// 返回结果JSON字符串
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取签名前准备参数
	 * 
	 * @param format
	 * @param timestamp
	 * @return
	 */
	public static String getSign(String format, long timestamp) {
		JSONObject dataSign = new JSONObject();
		dataSign.append("clientType", CLIENT_TYPE);
		dataSign.append("clientVersion", CLIENT_VERSION);
		dataSign.append("apiSecret", API_SECRET);
		dataSign.append("apiKey", API_KEY);
		dataSign.append("timestamp", timestamp);
		String formatData = getFormatData(dataSign, format);
		return md5(formatData);
	}

	/**
	 * 拼接替换生成签名前字符串
	 * 
	 * @param format
	 * @param timestamp
	 * @return
	 */
	public static String getFormatData(JSONObject data, String format) {
		Matcher matcher = VALUE_PATTERN.matcher(format);
		while (true) {
			while (matcher.find()) {
				String sign = matcher.group();
				String key = matcher.group(1);
				if (ObjectUtils.isNotEmpty(key)) {
					format = format.replace(sign, data.containsKey(key) && ObjectUtils.isNotEmpty(data.getStr(key)) && !"null".equalsIgnoreCase(data.getStr(key)) ? data.getStr(key) : "");
				} else {
					format = format.replace(sign, "");
				}
			}
			return format;
		}
	}

	/**
	 * 生成md5,小写
	 * 
	 * @param message
	 * @return
	 */
	public static String md5(String message) {
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes();

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			return byte2hex(buff);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 二进制转十六进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toLowerCase());
		}
		return sign.toString();
	}

}
