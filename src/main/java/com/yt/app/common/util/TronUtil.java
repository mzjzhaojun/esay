package com.yt.app.common.util;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TronUtil {

	// 测试tron
	public static String TestSendTron() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io
		ResponseEntity<Object> sov = resttemplate.exchange("http://192.168.110.129:8090/wallet/getaccount",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);
		return data;
	}

	public static String CreateAccount() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("owner_address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");
		map.add("account_address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io
		ResponseEntity<Object> sov = resttemplate.exchange("http://192.168.110.129:8090/wallet/getaccount",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);
		return data;
	}

	public static List<String> generateAddress() {
		// generate random address
		// SECP256K1.KeyPair kp = SECP256K1.KeyPair.generate();

		// SECP256K1.PublicKey pubKey = kp.getPublicKey();

//		Keccak.Digest256 digest = new Keccak.Digest256();
//		digest.update(pubKey.getEncoded(), 0, 64);
//		byte[] raw = digest.digest();
//		byte[] rawAddr = new byte[21];
//		rawAddr[0] = 0x41;
//		System.arraycopy(raw, 12, rawAddr, 1, 20);
//
//		List keyPairReturn = new ArrayList<String>();
//		keyPairReturn.add(Hex.toHexString(rawAddr));
//		keyPairReturn.add(Hex.toHexString(kp.getPrivateKey().getEncoded()));

		return null;
	}

}
