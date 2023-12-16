package com.yt.app.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtil {

	/**
	 * 加密算法RSA
	 */
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * 算法名称/加密模式/数据填充方式 默认：RSA/ECB/PKCS1Padding
	 */
	private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";

	/**
	 * Map获取公钥的key
	 */
	private static final String PUBLIC_KEY = "payboot:keys:publicKey";

	/**
	 * Map获取私钥的key
	 */
	private static final String PRIVATE_KEY = "payboot:keys:privateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 245;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 256;

	/**
	 * 1024 117 128 RSA 位数 如果采用2048 上面最大加密和最大解密则须填写: 245 256
	 */
	private static final int INITIALIZE_LENGTH = 2048;
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void InitKeys() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(INITIALIZE_LENGTH);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RedisUtil.set(PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RedisUtil.set(PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
	}

	/**
	 * 私钥解密
	 * 
	 * @param encryptedData 已加密数据
	 * @param privateKey    私钥(BASE64编码)
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		// base64格式的key字符串转Key对象
		Key privateK = KeyFactory.getInstance(KEY_ALGORITHM)
				.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHMS);
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		// 分段进行解密操作
		return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_DECRYPT_BLOCK);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data      源数据
	 * @param publicKey 公钥(BASE64编码)
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		// base64格式的key字符串转Key对象
		Key publicK = KeyFactory.getInstance(KEY_ALGORITHM)
				.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHMS);
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		// 分段进行加密操作
		return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data       源数据
	 * @param privateKey 私钥(BASE64编码)
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		Key privateK = KeyFactory.getInstance(KEY_ALGORITHM)
				.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHMS);
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
	}

	/**
	 * 公钥解密
	 * 
	 * @param encryptedData 已加密数据
	 * @param publicKey     公钥(BASE64编码)
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		Key publicK = KeyFactory.getInstance(KEY_ALGORITHM)
				.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHMS);
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_ENCRYPT_BLOCK);

	}

	/**
	 * 获取私钥
	 */
	public static String getPrivateKey() {
		return RedisUtil.get(PRIVATE_KEY);
	}

	/**
	 * 获取公钥
	 */
	public static String getPublicKey() {
		return RedisUtil.get(PUBLIC_KEY);
	}

	/**
	 * 分段进行加密、解密操作
	 */
	private static byte[] encryptAndDecryptOfSubsection(byte[] data, Cipher cipher, int encryptBlock) throws Exception {
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > encryptBlock) {
				cache = cipher.doFinal(data, offSet, encryptBlock);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * encryptBlock;
		}
		out.close();
		return out.toByteArray();
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data       已加密数据
	 * @param privateKey 私钥(BASE64编码)
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(privateKey);
		PrivateKey privateK = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateK);
		signature.update(data);
		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data      已加密数据
	 * @param publicKey 公钥(BASE64编码)
	 * @param sign      数字签名
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(publicKey);
		PublicKey publicK = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(keyBytes));
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64.decodeBase64(sign));
	}

}
