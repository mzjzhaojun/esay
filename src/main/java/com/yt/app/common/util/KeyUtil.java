package com.yt.app.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by dhcao on 2018/1/16. 读取证书文件为公私钥
 */
public class KeyUtil {

	private static KeyStore ks;

	static {
		try {
			ks = KeyStore.getInstance("PKCS12");
			// 获得密钥库文件流
			InputStream is = new FileInputStream("C:\\Users\\zj\\Downloads\\java\\bin\\user.pfx");
			// 加载密钥库
			ks.load(is, "123456".toCharArray());

			// 关闭密钥库文件流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取证书私钥字符串
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getPrimaryKey() throws Exception {
		String keyAlias = getKeyAlias();
		PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, "123456".toCharArray());
		String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
		System.out.println("privateKeyStr" + privateKeyStr);
		return privateKeyStr;
	}

	/**
	 * 获取证书公钥字符串
	 * 
	 * @return
	 * @throws Exception
	 */
	// public static String getPublicKey() throws Exception{
	// String keyAlias = getKeyAlias();
	// //公钥
	// Certificate certificate = ks.getCertificate(keyAlias);
	//
	// certificate.getPublicKey().getAlgorithm();
	// String publicKeyStr =
	// Base64.encodeBase64String(certificate.getPublicKey().getEncoded());
	// return publicKeyStr;
	// }
	//
	public static String getKeyAlias() throws Exception {
		@SuppressWarnings("rawtypes")
		Enumeration aliases = ks.aliases();
		String keyAlias = null;

		if (aliases.hasMoreElements()) {
			keyAlias = (String) aliases.nextElement();
		}
		return keyAlias;
	}

	public static void main(String[] args) {
		try {
			String keyAlias = getKeyAlias();
			PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, "123456".toCharArray());
			String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
			System.out.println(privateKeyStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
