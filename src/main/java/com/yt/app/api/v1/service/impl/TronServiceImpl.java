package com.yt.app.api.v1.service.impl;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.tron.trident.core.key.KeyPair;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronMapper;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.enums.YtDataSourceEnum;
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
		log.info("响应的消息:" + body);
		return true;
	}

	@Override
	public void createaccount(String owneraddress, String toaddress) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("owner_address", owneraddress);
		map.put("account_address", toaddress);
		map.put("visible", true);

		String sub_url = URL + "/wallet/createaccount";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);
	}

	@Override
	public void getaccount(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);

		String sub_url = URL + "/wallet/getaccount ";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);
	}

	@Override
	public void updateaccount(String name, String address) {

	}

	@Override
	public void getaccountbalance(String address, Integer block) {

	}

	@Override
	public void setaccountid(String address, Long accountid) {

	}

	@Override
	public void getaccountbyid(Long accountid) {

	}

	@Override
	public void createtransaction(String privatekey, String toaddress, String owneraddress, Integer amount) {
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

		broadcasttransaction(parame);
	}

	@Override
	public void broadcasttransaction(HashMap<String, Object> map) {
		String sub_url = URL + "/wallet/broadcasttransaction";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);
	}

	@Override
	public void broadcasthex(String address) {

	}

	@Override
	public void getsignweight(String signature, String txid) {

	}

	@Override
	public void getapprovedlist(String signature, String txid) {

	}

	@Override
	public void getaccountresource(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountresource";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);
	}

	@Override
	public void getaccountnet(String address) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("address", address);
		map.put("visible", true);
		String sub_url = URL + "/wallet/getaccountnet";
		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
		log.info("响应的消息:" + body);

	}

	@Override
	public void freezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
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

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		broadcasttransaction(parame);
	}

	@Override
	public void unfreezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource) {
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

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		broadcasttransaction(parame);
	}

	@Override
	public void cancelallunfreezev2(String privatekey, String owneraddress) {
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

		log.info("广播前打印请求参数:" + JSONUtil.toJsonPrettyStr(map));

		broadcasttransaction(parame);
	}

	// 将带宽或者能量资源代理给其它账户
	@Override
	public void delegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource, boolean lock, Integer lockperiod) {
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

		broadcasttransaction(parame);
	}

	// 取消为目标地址代理的带宽或者能量
	@Override
	public void undelegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource) {
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

		broadcasttransaction(parame);
	}

	@Override
	public void getnowblock() {

	}

	@Override
	public void getblock(String idornum) {

	}

	@Override
	public void getblockbynum(Integer num) {

	}

	@Override
	public void getblockbyid(String value) {

	}

	@Override
	public void getblockbylatestnum(Integer num) {

	}

	@Override
	public void getblockbalance(String hash, Integer number) {

	}

	@Override
	public void gettransactionbyid(String txid) {

	}

	@Override
	public void gettransactioninfobyid(String txid) {

	}

	@Override
	public void gettransactioncountbyblocknum(Integer number) {

	}

	@Override
	public void gettransactioninfobyblocknum(Integer number) {

	}

	@Override
	public void getnodeinfo() {

	}

}