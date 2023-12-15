package com.yt.app.common.util;

import java.util.Date;
import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.yt.app.common.base.context.AuthRsaKeyContext;

public class SecurityUtil {
	/**
	 * API解密
	 */
	public static String decrypt(String data) {
		try {
			// 后端RSA公钥加密后的AES的key
			String aesKey = AuthRsaKeyContext.getKey();
			// 后端私钥解密的到AES的key
			byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), RsaUtil.getPrivateKey());
			aesKey = new String(plaintext);
			data = AesUtil.decrypt(data, aesKey);
			System.out.println(data);
			return data;
		} catch (Throwable e) {
			// 输出到日志文件中
			throw new RuntimeException("ApiSecurityUtil.decrypt：解密异常！");
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
			serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
			String dataString = JSON.toJSONString(object, serializeConfig, SerializerFeature.PrettyFormat);
			String data = AesUtil.encrypt(dataString, AuthRsaKeyContext.getAesKey());
			System.out.println(dataString);
			return data;
		} catch (Throwable e) {
			// 输出到日志文件中
			throw new RuntimeException("ApiSecurityUtil.encrypt：加密异常！");
		}
	}
}