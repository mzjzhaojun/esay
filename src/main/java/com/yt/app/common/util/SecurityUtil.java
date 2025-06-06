package com.yt.app.common.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.exption.YtException;

public class SecurityUtil {
	/**
	 * API解密
	 */
	public static String decrypt(String data) {
		try {
			String aesKey = AuthContext.getKey();
			byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), RsaUtil.getPrivateKey());
			aesKey = new String(plaintext);
			data = AesUtil.decrypt(data, aesKey);
			return data;
		} catch (Throwable e) {
			throw new YtException("长时间未登录！", YtCodeEnum.YT401);
		}
	}

	/**
	 * API加密
	 */
	public static String encrypt(Object object) {
		try {
			SerializeConfig serializeConfig = new SerializeConfig();
			serializeConfig.put(Long.class, ToStringSerializer.instance);
			serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
			serializeConfig.put(Date.class, new SimpleDateFormatSerializer("YY-MM-dd HH:mm:ss"));
			String dataString = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
			return AesUtil.encrypt(dataString, AuthContext.getAesKey());
		} catch (Throwable e) {
			throw new YtException("用户长时间未登录！", YtCodeEnum.YT401);
		}
	}

	public static String cleanXSS(String src) {
		String temp = src;

		src = src.replaceAll("<", "＜").replaceAll(">", "＞");
		src = src.replaceAll("'", "＇");
		src = src.replaceAll(";", "；");

		/** -----------------------start-------------------------- */
		src = src.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
		src = src.replaceAll("eval\\((.*)\\)", "");
		src = src.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		src = src.replaceAll("script", "");
		src = src.replaceAll("link", "");
		src = src.replaceAll("frame", "");
		/** -----------------------end-------------------------- */
		Pattern pattern = Pattern.compile("(eval\\((.*)\\)|script)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(src);
		src = matcher.replaceAll("");

		pattern = Pattern.compile("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(src);
		src = matcher.replaceAll("\"\"");

		// 增加脚本
		src = src.replaceAll("script", "").replaceAll(";", "").replaceAll("0x0d", "").replaceAll("0x0a", "");

		if (!temp.equals(src)) {
			throw new YtException("xss攻击检查：参数含有非法攻击字符，已禁止继续访问");
		}
		return src;
	}
}