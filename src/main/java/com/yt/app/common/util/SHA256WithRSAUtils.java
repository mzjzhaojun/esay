/****************************************************************** 
 *
 *    Powered By tianxia-online. 
 *
 *    Copyright (c) 2018-2020 Digital Telemedia 天下科技 
 *    http://www.d-telemedia.com/ 
 *
 *    Package:     com.tx.platform.utils 
 *
 *    Filename:    SHA256WithRSAUtils.java 
 *
 *    Description: TODO(用一句话描述该文件做什么) 
 *
 *    Copyright:   Copyright (c) 2018-2020 
 *
 *    Company:     天下科技 
 *
 *    @author:     Finlay 
 *
 *    @version:    1.0.0 
 *
 *    Create at:   2019年05月17日 21:50 
 *
 *    Revision: 
 *
 *    2019/5/17 21:50 
 *        - first revision 
 *
 *****************************************************************/
package com.yt.app.common.util;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;

public class SHA256WithRSAUtils {

	public static final String CHARSET = "UTF-8";
	// 密钥算法
	public static final String ALGORITHM_RSA = "RSA";
	// RSA 签名算法
	public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
	public static final int ALGORITHM_RSA_PRIVATE_KEY_LENGTH = 2048;

	private SHA256WithRSAUtils() {
	}

	/**
	 * 初始化RSA算法密钥对
	 *
	 * @param keysize RSA1024已经不安全了,建议2048
	 * @return 经过Base64编码后的公私钥Map, 键名分别为publicKey和privateKey
	 */
	public static Map<String, String> initRSAKey(int keysize) {
		if (keysize != ALGORITHM_RSA_PRIVATE_KEY_LENGTH) {
			throw new IllegalArgumentException("RSA1024已经不安全了,请使用" + ALGORITHM_RSA_PRIVATE_KEY_LENGTH + "初始化RSA密钥对");
		}
		// 为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM_RSA + "]");
		}
		// 初始化KeyPairGenerator对象,不要被initialize()源码表面上欺骗,其实这里声明的size是生效的
		kpg.initialize(ALGORITHM_RSA_PRIVATE_KEY_LENGTH);
		// 生成密匙对
		KeyPair keyPair = kpg.generateKeyPair();
		// 得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		// 得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		Map<String, String> keyPairMap = new HashMap<String, String>();
		keyPairMap.put("publicKey", publicKeyStr);
		keyPairMap.put("privateKey", privateKeyStr);
		return keyPairMap;
	}

	/**
	 * RSA算法公钥加密数据
	 *
	 * @param data 待加密的明文字符串
	 * @param key  RSA公钥字符串
	 * @return RSA公钥加密后的经过Base64编码的密文字符串
	 */
	public static String buildRSAEncryptByPublicKey(String data, String key) {
		try {
			// 通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			Key publicKey = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法公钥解密数据
	 *
	 * @param data 待解密的经过Base64编码的密文字符串
	 * @param key  RSA公钥字符串
	 * @return RSA公钥解密后的明文字符串
	 */
	public static String buildRSADecryptByPublicKey(String data, String key) {
		try {
			// 通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			Key publicKey = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法私钥加密数据
	 *
	 * @param data 待加密的明文字符串
	 * @param key  RSA私钥字符串
	 * @return RSA私钥加密后的经过Base64编码的密文字符串
	 */
	public static String buildRSAEncryptByPrivateKey(String data, String key) {
		try {
			// 通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法私钥解密数据
	 * 
	 * @param data 待解密的经过Base64编码的密文字符串
	 * @param key  RSA私钥字符串
	 * @return RSA私钥解密后的明文字符串
	 */
	public static String buildRSADecryptByPrivateKey(String data, String key) {
		try {
			// 通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法使用私钥对数据生成数字签名
	 *
	 * @param data 待签名的明文字符串
	 * @param key  RSA私钥字符串
	 * @return RSA私钥签名后的经过Base64编码的字符串
	 */
	public static String buildRSASignByPrivateKey(String data, String key) {
		try {
			// 通过PKCS#8编码的Key指令获得私钥对象
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initSign(privateKey);
			signature.update(data.getBytes(CHARSET));
			return Base64.getEncoder().encodeToString(signature.sign());
		} catch (Exception e) {
			throw new RuntimeException("签名字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法使用公钥校验数字签名
	 *
	 * @param data 参与签名的明文字符串
	 * @param key  RSA公钥字符串
	 * @param sign RSA签名得到的经过Base64编码的字符串
	 * @return true--验签通过,false--验签未通过
	 */
	public static boolean buildRSAverifyByPublicKey(String data, String key, String sign) {
		try {
			// 通过X509编码的Key指令获得公钥对象
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(key));
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
			PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
			Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
			signature.initVerify(publicKey);
			signature.update(data.getBytes(CHARSET));
			return signature.verify(Base64.getDecoder().decode(sign));
		} catch (Exception e) {
			throw new RuntimeException("验签字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * RSA算法分段加解密数据
	 *
	 * @param cipher 初始化了加解密工作模式后的javax.crypto.Cipher对象
	 * @param opmode 加解密模式,值为javax.crypto.Cipher.ENCRYPT_MODE/DECRYPT_MODE
	 * @return 加密或解密后得到的数据的字节数组
	 */
	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8;
		} else {
			maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		IOUtils.closeQuietly(out);
		return resultDatas;
	}

	public static void main(String[] args) {
		String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDWBreWeGHhdvaWAbuf2ZFJDaDhcga6RYxzWYkookmZMGuc/oT+/2atfVVoDG8Ns9W6mNDh2XiqXdUH+C57hXo3bjYwcZXp/xTz80veb2f1jhCNjCoNyBa4uU9GZP0CX2CzMR/6drkzoiVF/oKlXm0OA4fLUsfYHWL6cLebYefY04p0RjMOhsatv1vgEMSxsH+0EVvAylRSnoWYhXjdwVQiC0tJi62BcOiRCRcQnC1QxrjuW96LVRNN9ixyuA4LPaZF0nCNIDze7qABjpma/rltcYzGqWtGngSRjaxh14lLaZGmipe0PkN/a5cKnfqUrK7/XlkUjRuJEAYHUCvcC007AgMBAAECggEBAM1nmvBVTpMlKhjjRt9t3gp/guR7G8vPjbDywDj4L6xOlSkR+DAGi9N7EumN7VL4QeoYWXg3z5+p+Bby8CFqA9aI9obdrCPrxH8wA6NRxjMbifVfDMk3O99DMbr8lvmc8eBoCCnHoYzVU/JuBZKSIhWt+7h+FP/rjB+Q6BW/C9TBxqIWviJ8kvXLkk4j2KgXde8r8PvNbBur51zQbNvIM/u+0urLMH38RkUL7UceqWZXwyMF03OGh8W3mTNzLu9eIsQS9lKu5SD46iQhCQtLESmnoHNIETBT2UWhBEt+EWDnZqwD8nA5fLtlV1R1WkKAx1GTMbKHKltcRa6OR53UYmkCgYEA+t1v7ubCvvNHdCRzx+ADmhLs4BIJPfprecBttJZS9JxbbdLSM5AHBRYEiFrzKK9G6126wfjPebzKyZ8Hpq/d3UQQQwn0pt1RXmoUPCGeplivx0zTXoEMRXv1qJQCuvQe6KW/KoM1Se9/4yyskSj17Zih0ItKZbASsJhwZkcWibcCgYEA2mg9i5f27wZxklelT9WBQbv7R8kFdT1XKA4xr84rRQN4uFVYRZ3Tc2yNPFiPWHaxXgYCKY4AARYO6WCrU6KEFhYhdVLLsa8QOrBFyUlx3gtMe0cL9xexSJaWePtBtyx/v7JQzJ+iKcLnL5RzHcYK4MOxRB0z7Ik5b/moemjt6J0CgYBjUyFqWXtp1zDHT1d4H73NVwjqfrmiDHBZOFM3WP1nVDDu/rWzLKda4CjTRA9aYGuX7PhwrG6ZKKKxyYFiAee6H6TApkRzfNUmqKFyXW1PN3YBVu2pUTbmm/nb0R5znwxN2LIjxudmaqkb7Sg2IR2eAPywcTX3pQsBWJnIJQVLaQKBgBREzEFiLvZrwKp2G89W3xH8ogs9apZAxQ/gkVfhaD6VXeStdu2vAeqaTlklVtg826UAJb85gGKZa3TZFc1DLcLm9t/ueRzaREVOCovtlvWmUsi2BTQ/LKKx5vD/UE/xf0GJCNqUjfYWMmJGApDmyielwZOyqhjvezcbEjmZ90o5AoGASHscipngFAL1DwbpiZ2wHb1EpwHBYpaW1vBpn1lIPRyp7ynRULV6gPRypwOKmGufVKVKXvrxHjaap5e7lrRGT5iAmChpHBkLmYuKk1xzHTvoRdoW9SYfvEFc/UniwAqpP5oqozvvbniDwVF0qGmCfajSd8HjEGBPIE0ceHw1Onc=";
//        String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiqoMbM2+Dm7qCeVPA3c9srThRXPNX5p4kRaPo7zbaznoDFKXfYAT7zBGgc3XiQu+AoPx7ABO3/btuADy4tKC2GQsLYYbNNcUyIQIrPIeyGAknVq3G5/IKQe2qUnuFHdHUus5LkXA01RrCza8zTjCh30/Etd3bbKg8gwQYUqZAcHvU5Hi0AfCuWYw2CfLk7bK3HsveXjRXttq/KgIb+etslAUxtD42aUJoiVg9E+lESy8zWBDlxM7FVWYDygTVklWbzIy4N9nhb/9jMPsfN5+OMN/RS8ehN+OwOYVUGFmwS7hw8hVM1v3p3TpjEo9WCZhg4XLYBCvlCANWzW3sWKN9wIDAQAB";
		String str = "bizContent={\"accountBookId\":\"191578888864792576\",\"orderId\":\"out2506131959505040\",\"orderAmount\":\"111.00\",\"title\":\"title\",\"payee\":{\"identityType\":\"BANKCARD\",\"identity\":\"62121231\",\"name\":\"测试\",\"accountType\":\"2\"}}&mchId=2800250612119097&signType=RSA&timestamp=2025-06-13 19:59:50&version=1.0";
		System.out.println(buildRSASignByPrivateKey(str, privateKey));
//        System.out.println("校验:"+buildRSAverifyByPublicKey("ABCabc123测试",publicKey,buildRSASignByPrivateKey("ABCabc123测试", privateKey)));
//
	}

}
