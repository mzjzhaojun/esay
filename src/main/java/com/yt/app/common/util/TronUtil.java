package com.yt.app.common.util;

import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.crypto.tuwenitypes.Bytes32;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;
import sun.security.provider.SecureRandom;

@Slf4j
public class TronUtil {

	private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH = ImmutableList
			.of(new ChildNumber(44, true), new ChildNumber(195, true), ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

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

	public static List<String> generateAddress(List<String> stringList) {
		List<String> listaddress = new ArrayList<String>();
		try {
			byte[] seed = MnemonicCode.toSeed(stringList, "");
			DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
			DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
			DeterministicKey deterministicKey = deterministicHierarchy.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false,
					true, new ChildNumber(0));
			byte[] byte2 = deterministicKey.getPrivKeyBytes();
			SECP256K1.PrivateKey privateKey = SECP256K1.PrivateKey.create(Bytes32.wrap(byte2));
			SECP256K1.KeyPair keyPair2 = SECP256K1.KeyPair.create(privateKey);
			KeyPair keyPair1 = new KeyPair(keyPair2);
			listaddress.add(keyPair1.toPrivateKey());
			listaddress.add(keyPair1.toHexAddress());
			listaddress.add(keyPair1.toBase58CheckAddress());
		} catch (Exception e) {
			return null;
		}
		return listaddress;
	}

	public static List<String> mnemoniCode() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] bytes = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
		secureRandom.engineNextBytes(bytes);
		try {
			List<String> stringList = MnemonicCode.INSTANCE.toMnemonic(bytes);
			return stringList;
		} catch (MnemonicLengthException e) {
			e.printStackTrace();
		}
		return null;
	}
}
