package com.yt.app.api.v1.service.impl;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.tron.trident.core.key.KeyPair;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronMapper;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.util.TronUtil;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 15:25:57
 */
@Slf4j
@Service
public class TronServiceImpl extends YtBaseServiceImpl<Tron, Long> implements TronService {

	@Autowired
	private TronMapper mapper;

	// final static String URL = "https://nile.trongrid.io";
	final static String URL = "https://api.trongrid.io";

	@Override
	@Transactional
	public Integer post(Tron t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tron> list(Map<String, Object> param) {
		List<Tron> list = mapper.list(param);
		return new YtPageBean<Tron>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tron get(Long id) {
		Tron t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TronVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TronVO>(Collections.emptyList());
		}
		List<TronVO> list = mapper.page(param);
		return new YtPageBean<TronVO>(param, list, count);
	}

	@Override
	public boolean validateaddress(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);

		String sub_url = URL + "/wallet/validateaddress";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("验证地址响应的消息:" + body);
		return true;
	}

	@Override
	public String createaccount(String owneraddress, String toaddress) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("account_address", toaddress);
		map.put("visible", true);

		String sub_url = URL + "/wallet/createaccount";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("创建账户响应的消息:" + body);
		return body;
	}

	@Override
	public String getaccount(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);

		String sub_url = URL + "/wallet/getaccount ";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("获取账户信息响应的消息:" + body);
		return body;
	}

	@Override
	public String updateaccount(String name, String owneraddress) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("account_name", name);
		map.put("owner_address", owneraddress);
		map.put("visible", true);

		String sub_url = URL + "/wallet/updateaccount  ";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("更新账户响应的消息:" + body);
		return body;
	}

	@Override
	public String getaccountbalance(String address, Integer block) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("hash", block);
		map.put("visible", true);

		String sub_url = URL + "/wallet/getaccountbalance  ";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户余额响应的消息:" + body);
		return body;
	}

	@Override
	public String setaccountid(String address, Long accountid) {
		return null;
	}

	@Override
	public String getaccountbyid(Long accountid) {
		return null;
	}

	@Override
	public String createtransaction(String privatekey, String toaddress, String owneraddress, BigInteger amount) {
		KeyPair keyPair = new KeyPair(privatekey);
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("to_address", toaddress);
		map.put("amount", amount);
		map.put("visible", true);

		String sub_url = URL + "/wallet/createtransaction";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("创建交易,响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);

		String error = obj.getStr("Error");
		if (!isEmpty(error) && error.contains("balance is not sufficient")) {
			// 抛自己的异常(余额不足)
			System.out.println("balance is not sufficient");
			throw new YtException("balance is not sufficient");
		} else {
			// 签名交易
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

			log.info("trx交易广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

			return broadcasttransaction(parame);
		}
	}

	public boolean isEmpty(String content) {
		if (content == null || content.length() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String broadcasttransaction(HashMap<String, Object> map) {
		String sub_url = URL + "/wallet/broadcasttransaction";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("trx交易广播响应的消息:" + body);
		return body;
	}

	@Override
	public String broadcasthex(String address) {
		return null;
	}

	@Override
	public String getsignweight(String signature, String txid) {
		return null;
	}

	@Override
	public String getapprovedlist(String signature, String txid) {
		return null;
	}

	@Override
	public String getaccountresource(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountresource";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户资源响应的消息:" + body);
		return body;
	}

	@Override
	public String getaccountnet(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountnet";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询账户宽带响应的消息:" + body);
		return body;
	}

	@Override
	public String freezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
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

		log.info("质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		return broadcasttransaction(parame);
	}

	@Override
	public String unfreezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
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

		log.info("解锁质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		return broadcasttransaction(parame);
	}

	@Override
	public String cancelallunfreezev2(String privatekey, String owneraddress) {
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

		log.info("取消质押广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		return broadcasttransaction(parame);
	}

	// 将带宽或者能量资源代理给其它账户
	@Override
	public String delegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource, boolean lock, Integer lockperiod) {
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

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		return broadcasttransaction(parame);
	}

	// 取消为目标地址代理的带宽或者能量
	@Override
	public String undelegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("receiver_address", receiveraddress);
		map.put("balance", balance);
		map.put("resource", resource);
		map.put("visible", true);
		String sub_url = URL + "/wallet/undelegateresource";
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

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		return broadcasttransaction(parame);
	}

	// 查询最新块响应的消息
	@Override
	public String getnowblock() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("visible", true);
		String sub_url = URL + "/wallet/getnowblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	// 查询块响应的消息
	@Override
	public String getblock(String idornum) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("id_or_num", idornum); // 区块高度或者区块哈希，不设置表示查询最新区块
		map.put("detail", true);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	// 查询块响应的消息
	@Override
	public String getblockbynum(BigInteger num) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("num", num); // 块高度
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblock";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	@Override
	public String getblockbyid(String value) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", value); // ID查询块
		map.put("visible", true);
		String sub_url = URL + "/wallet/getblockbyid";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查询块响应的消息:" + body);
		return body;
	}

	@Override
	public String getblockbylatestnum(Integer num) {
		return null;
	}

	@Override
	public String getblockbalance(String hash, Integer number) {
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
	@Override
	public String gettransactionbyid(String txid) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("value", txid);
		map.put("visible", true);
		String sub_url = URL + "/wallet/gettransactionbyid";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	@Override
	public String gettransactioninfobyid(String txid) {
		return null;
	}

	@Override
	public String gettransactioncountbyblocknum(Integer number) {
		return null;
	}

	// 获取特定区块的所有交易 Info 信息响应的消息
	@Override
	public String gettransactioninfobyblocknum(BigInteger number) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("num", number);
		String sub_url = URL + "/wallet/gettransactioninfobyblocknum";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		return body;
	}

	@Override
	public String getnodeinfo() {
		HashMap<String, Object> map = new HashMap<>();
		String sub_url = URL + "/wallet/getnodeinfo";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("查看当前连接节点的信息响应的消息:" + body);
		return body;
	}

	@Override
	public String triggerconstantcontract(String owner_address, String contract_address, String parameter) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owner_address);
		map.put("contract_address", contract_address);
		map.put("function_selector", "balanceOf(address)");
		map.put("parameter", parameter);
		String sub_url = URL + "/wallet/triggerconstantcontract";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("调用常量合约响应的消息:" + body);
		return body;
	}

	@Override
	public String triggersmartcontract(String privatekey, String owner_address, String contract_address, String parameter, long fee_limit, Integer call_value) {
		KeyPair keyPair = new KeyPair(privatekey);

		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owner_address);
		map.put("contract_address", contract_address);
		map.put("function_selector", "transfer(address,uint256)");
		map.put("parameter", parameter);
		map.put("call_value", call_value);
		map.put("fee_limit", fee_limit);
		String sub_url = URL + "/wallet/triggersmartcontract";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("调用TRC20合约响应的消息:" + body);

		JSONObject obj = JSONUtil.parseObj(body);

		String error = obj.getStr("Error");
		if (!isEmpty(error) && error.contains("balance is not sufficient")) {
			// 抛自己的异常(余额不足)
			System.out.println("balance is not sufficient");
			throw new YtException("balance is not sufficient");
		} else {
			// 签名交易
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

			log.info("trx交易广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

			return broadcasttransaction(parame);
		}
	}

	@Override
	public String getTest() {
//		HashMap<String, Object> map = new HashMap<>();
//		String sub_url = "http://192.168.18.4:8080/esay/rest/v1/order/income/1845491796000641024";
//		String body = HttpRequest.get(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
//		log.info("getTest:" + body);
//		return body;
		Tron t = new Tron();
		t.setAddress("1231231");
		t.setBalance(111.11);
		mapper.post(t);
		Tron tron = mapper.get(t.getId());
		System.out.println(tron);
		return "";
	}

}