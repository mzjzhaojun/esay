package com.yt.app.common.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.crypto.tuwenitypes.Bytes32;

import com.google.common.collect.ImmutableList;

import sun.security.provider.SecureRandom;

public class TronUtil {

	private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH = ImmutableList
			.of(new ChildNumber(44, true), new ChildNumber(195, true), ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

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

	public static String bytesToHex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

}
