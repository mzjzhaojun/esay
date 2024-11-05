package com.yt.app.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Base58;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.spongycastle.util.encoders.Hex;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.crypto.tuwenitypes.Bytes32;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;
import sun.security.provider.SecureRandom;

@Slf4j
public class TronUtil {

	private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH = ImmutableList.of(new ChildNumber(44, true), new ChildNumber(195, true), ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);
	static int ADDRESS_SIZE = 21;
	private static byte addressPreFixByte = (byte) 0x41; // 41 + address (byte) 0xa0; //a0 + address

	public static List<String> generateAddress(List<String> stringList) {
		List<String> listaddress = new ArrayList<String>();
		try {
			byte[] seed = MnemonicCode.toSeed(stringList, "");
			DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
			DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
			DeterministicKey deterministicKey = deterministicHierarchy.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
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

	/**
	 * 转换成hex地址
	 *
	 * @param address
	 * @return
	 */
	public static String toHexAddress(String address) {
		if (StringUtils.isEmpty(address)) {
			throw new IllegalArgumentException("传入的地址不可为空");
		}
		if (!address.startsWith("T")) {
			throw new IllegalArgumentException("传入地址不合法:" + address);
		}
		return Hex.toHexString(decodeFromBase58Check(address));
	}

	public static String encode58Check(byte[] input) {
		try {
			byte[] hash0 = hash(true, input);
			byte[] hash1 = hash(true, hash0);
			byte[] inputCheck = new byte[input.length + 4];
			System.arraycopy(input, 0, inputCheck, 0, input.length);
			System.arraycopy(hash1, 0, inputCheck, input.length, 4);
			return Base58.encode(inputCheck);
		} catch (Throwable t) {
			log.error(String.format("data error:%s", Hex.toHexString(input)), t);
		}
		return null;
	}

	/**
	 * Calculates the SHA-256 hash of the given bytes.
	 *
	 * @param input the bytes to hash
	 * @return the hash (in big-endian order)
	 */
	public static byte[] hash(boolean isSha256, byte[] input) throws NoSuchAlgorithmException {
		return hash(isSha256, input, 0, input.length);
	}

	/**
	 * Calculates the SHA-256 hash of the given byte range.
	 *
	 * @param input  the array containing the bytes to hash
	 * @param offset the offset within the array of the bytes to hash
	 * @param length the number of bytes to hash
	 * @return the hash (in big-endian order)
	 */
	public static byte[] hash(boolean isSha256, byte[] input, int offset, int length) throws NoSuchAlgorithmException {
		if (isSha256) {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(input, offset, length);
			return digest.digest();
		} else {
			SM3Digest digest = new SM3Digest();
			digest.update(input, offset, length);
			byte[] eHash = new byte[digest.getDigestSize()];
			digest.doFinal(eHash, 0);
			return eHash;
		}
	}

	public static byte[] decodeFromBase58Check(String addressBase58) {
		try {
			byte[] address = decode58Check(addressBase58);
			if (!addressValid(address)) {
				return null;
			}
			return address;
		} catch (Throwable t) {
			log.error(String.format("decodeFromBase58Check-error:" + addressBase58), t);
		}
		return null;
	}

	private static byte[] decode58Check(String input) throws Exception {
		byte[] decodeCheck = Base58.decode(input);
		if (decodeCheck.length <= 4) {
			return null;
		}
		byte[] decodeData = new byte[decodeCheck.length - 4];
		System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
		byte[] hash0 = hash(true, decodeData);
		byte[] hash1 = hash(true, hash0);
		if (hash1[0] == decodeCheck[decodeData.length] && hash1[1] == decodeCheck[decodeData.length + 1] && hash1[2] == decodeCheck[decodeData.length + 2] && hash1[3] == decodeCheck[decodeData.length + 3]) {
			return decodeData;
		}
		return null;
	}

	private static boolean addressValid(byte[] address) {
		if (ArrayUtils.isEmpty(address)) {
			return false;
		}
		if (address.length != ADDRESS_SIZE) {
			return false;
		}
		byte preFixbyte = address[0];
		return preFixbyte == addressPreFixByte;
		// Other rule;
	}

}
