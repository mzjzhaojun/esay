package com.yt.app.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;

import org.bouncycastle.util.encoders.Hex;
import org.tron.trident.core.key.KeyPair;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TronUtils {

	final static BigDecimal decimal = new BigDecimal("1000000");
	final static String URL = "http://192.168.110.231:8090";

	public static boolean validateaddress(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);

		String sub_url = URL + "/wallet/validateaddress";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("验证地址响应的消息:" + body);
		return true;
	}

	public static String createaccount(String owneraddress, String toaddress) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("account_address", toaddress);
		map.put("visible", true);

		String sub_url = URL + "/wallet/createaccount";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("创建账户响应的消息:" + body);
		return body;
	}

	public static String getaccount(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccount";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		BigInteger balance = BigInteger.ZERO;
		JSONObject obj = JSONUtil.parseObj(body);
		BigInteger b = obj.getBigInteger("balance");
		if (b != null) {
			balance = b;
		}
		return new BigDecimal(balance).divide(decimal, 6, RoundingMode.FLOOR).toString();
	}

	public static String updateaccount(String name, String owneraddress) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("account_name", name);
		map.put("owner_address", owneraddress);
		map.put("visible", true);

		String sub_url = URL + "/wallet/updateaccount";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("更新账户响应的消息:" + body);
		return body;
	}

	public static String getaccountbalance(String address, Integer block) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("hash", block);
		map.put("visible", true);

		String sub_url = URL + "/wallet/getaccountbalance";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户余额响应的消息:" + body);
		return body;
	}

	public static String setaccountid(String address, Long accountid) {
		return null;
	}

	public static String getaccountbyid(Long accountid) {
		return null;
	}

	public static String getchainparameters() {
		HashMap<String, Object> map = new HashMap<>();
		String sub_url = URL + "/wallet/getchainparameters";
		String body = HttpRequest.get(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询参数响应的消息:" + body);
		return body;
	}

	public static boolean isEmpty(String content) {
		if (content == null || content.length() == 0) {
			return true;
		}
		return false;
	}

	public static String broadcasttransaction(HashMap<String, Object> map) {
		String sub_url = URL + "/wallet/broadcasttransaction";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String broadcasthex(HashMap<String, Object> map) {
		String sub_url = URL + "/wallet/broadcasthex";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String getsignweight(String signature, String txid) {
		return null;
	}

	public static String getapprovedlist(String signature, String txid) {
		return null;
	}

	public static String getaccountresource(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountresource";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户资源响应的消息:" + body);
		return body;
	}

	public static String getaccountnet(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountnet";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户宽带响应的消息:" + body);
		return body;
	}

	public static String freezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("frozen_balance", frozenbalance);
		map.put("resource", resource);
		map.put("visible", true);
		String sub_url = URL + "/wallet/freezebalancev2";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);
		String txId = obj.getStr("txID");
		byte[] decode = Hex.decode(txId);
		byte[] bytes = KeyPair.signTransaction(decode, keyPair);
		String signatureHex = TronUtil.bytesToHex(bytes);

		HashMap<String, Object> parame = new HashMap<>();
		parame.put("signature", signatureHex);
		parame.put("txID", txId);
		parame.put("visible", true);
		parame.put("raw_data", obj.getJSONObject("raw_data"));
		parame.put("raw_data_hex", obj.getStr("raw_data_hex"));

		log.info("质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(parame));

		return broadcasttransaction(parame);
	}

	public static String unfreezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("frozen_balance", frozenbalance);
		map.put("resource", resource);
		map.put("visible", true);
		String sub_url = URL + "/wallet/unfreezebalancev2";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);
		String txId = obj.getStr("txID");
		byte[] decode = Hex.decode(txId);
		byte[] bytes = KeyPair.signTransaction(decode, keyPair);
		String signatureHex = TronUtil.bytesToHex(bytes);

		HashMap<String, Object> parame = new HashMap<>();
		parame.put("signature", signatureHex);
		parame.put("txID", txId);
		parame.put("visible", true);
		parame.put("raw_data", obj.getJSONObject("raw_data"));
		parame.put("raw_data_hex", obj.getStr("raw_data_hex"));

		log.info("解锁质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(parame));

		return broadcasttransaction(parame);
	}

	public static String cancelallunfreezev2(String privatekey, String owneraddress) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("visible", true);
		String sub_url = URL + "/wallet/cancelallunfreezev2";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);
		String txId = obj.getStr("txID");
		byte[] decode = Hex.decode(txId);
		byte[] bytes = KeyPair.signTransaction(decode, keyPair);
		String signatureHex = TronUtil.bytesToHex(bytes);

		HashMap<String, Object> parame = new HashMap<>();
		parame.put("signature", signatureHex);
		parame.put("txID", txId);
		parame.put("visible", true);
		parame.put("raw_data", obj.getJSONObject("raw_data"));
		parame.put("raw_data_hex", obj.getStr("raw_data_hex"));

		log.info("取消质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(parame));

		return broadcasttransaction(parame);
	}

	// 将带宽或者能量资源代理给其它账户

	public static String delegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource, boolean lock, Integer lockperiod) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("receiver_address", receiveraddress);
		map.put("balance", balance);
		map.put("resource", resource);
		map.put("lock", lock);
		map.put("lock_period", lockperiod);
		map.put("visible", true);
		String sub_url = URL + "/wallet/delegateresource";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);
		String txId = obj.getStr("txID");
		byte[] decode = Hex.decode(txId);
		byte[] bytes = KeyPair.signTransaction(decode, keyPair);
		String signatureHex = TronUtil.bytesToHex(bytes);

		HashMap<String, Object> parame = new HashMap<>();
		parame.put("signature", signatureHex);
		parame.put("txID", txId);
		parame.put("visible", true);
		parame.put("raw_data", obj.getJSONObject("raw_data"));
		parame.put("raw_data_hex", obj.getStr("raw_data_hex"));

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(parame));

		return broadcasttransaction(parame);
	}

	// 取消为目标地址代理的带宽或者能量

	public static String undelegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("receiver_address", receiveraddress);
		map.put("balance", balance);
		map.put("resource", resource);
		map.put("visible", true);
		String sub_url = URL + "/wallet/undelegateresource";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();

		JSONObject obj = JSONUtil.parseObj(body);
		String txId = obj.getStr("txID");
		byte[] decode = Hex.decode(txId);
		byte[] bytes = KeyPair.signTransaction(decode, keyPair);
		String signatureHex = TronUtil.bytesToHex(bytes);

		HashMap<String, Object> parame = new HashMap<>();
		parame.put("signature", signatureHex);
		parame.put("txID", txId);
		parame.put("visible", true);
		parame.put("raw_data", obj.getJSONObject("raw_data"));
		parame.put("raw_data_hex", obj.getStr("raw_data_hex"));

		return broadcasttransaction(parame);
	}

	// 查询最新块响应的消息

	public static String getnowblock() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("visible", true);
		String sub_url = URL + "/wallet/getnowblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	// 查询块响应的消息

	public static String getblock(String idornum) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("id_or_num", idornum); // 区块高度或者区块哈希，不设置表示查询最新区块
		map.put("detail", true);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	// 查询块响应的消息

	public static String getblockbynum(BigInteger num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("num", num); // 块高度
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String getblockbyid(String value) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", value); // ID查询块
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblockbyid";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询块响应的消息:" + body);
		return body;
	}

	public static String getblockbylatestnum(Integer num) {
		return null;
	}

	public static String getblockbalance(String hash, Integer number) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("hash", hash); // ID查询块
		map.put("number", number);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblockbalance";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询块余额变化操作响应的消息:" + body);
		return body;
	}

	// 通过ID查询交易响应的消

	public static String gettransactionbyid(String txid) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", txid);
		map.put("visible", true);
		String sub_url = URL + "/wallet/gettransactionbyid";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String gettransactioninfobyid(String txid) {
		return null;
	}

	public static String gettransactioncountbyblocknum(Integer number) {
		return null;
	}

	// 获取特定区块的所有交易 Info 信息响应的消息

	public static String gettransactioninfobyblocknum(BigInteger number) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("num", number);
		String sub_url = URL + "/wallet/gettransactioninfobyblocknum";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String getnodeinfo() {
		HashMap<String, Object> map = new HashMap<>();
		String sub_url = URL + "/wallet/getnodeinfo";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查看当前连接节点的信息响应的消息:" + body);
		return body;
	}

	public static String getcontract(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", address);
		String sub_url = URL + "/wallet/getcontract";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	public static String triggerconstantcontract(String owner_address, String contract_address, String parameter) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owner_address);
		map.put("contract_address", contract_address);
		map.put("function_selector", "balanceOf(address)");
		map.put("parameter", parameter);
		String sub_url = URL + "/wallet/triggerconstantcontract";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

}
