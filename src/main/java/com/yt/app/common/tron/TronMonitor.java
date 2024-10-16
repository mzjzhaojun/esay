package com.yt.app.common.tron;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.FunctionEncoder;

import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.common.util.TransformUtil;
import com.yt.app.common.util.TronUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import java.math.RoundingMode;

@Slf4j
@Component
public class TronMonitor {

	@Autowired
	private TronService tronservice;
	@Autowired
	private TronmemberorderMapper tronmemberordermapper;

	private BigDecimal decimal = new BigDecimal("1000000");

	private String contract = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";

	private static String owneraddress = "TUrntwm5t9umKhC7jv89RXGo33qcTFAAAA";

	private static String privatekey = "63d99b74511082f06e3f5f4b6e02e663c9a43939525368060da19b704f2b9aa4";

	private static BigInteger TRC20_TARGET_BLOCK_NUMBER;
	private static BigInteger CURRENT_SYNC_BLOCK_NUMBER;
	private static BigInteger BLOCKS = new BigInteger("15");

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		if (TRC20_TARGET_BLOCK_NUMBER == null) {
			String responseString = tronservice.getnowblock();
			JSONObject jsonObject = JSONUtil.parseObj(responseString);
			BigInteger blockNumber = jsonObject.getJSONObject("block_header").getJSONObject("raw_data").getBigInteger("number");
			CURRENT_SYNC_BLOCK_NUMBER = blockNumber.subtract(BLOCKS);
		}
	}

	/**
	 * 归集，自定义执行时间
	 *
	 * @throws Throwable
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void charge() throws Throwable {
		if (CURRENT_SYNC_BLOCK_NUMBER != null) {
			String responseString = tronservice.getnowblock();
			JSONObject jsonObject = JSONUtil.parseObj(responseString);
			BigInteger blockNumber = jsonObject.getJSONObject("block_header").getJSONObject("raw_data").getBigInteger("number");
			TRC20_TARGET_BLOCK_NUMBER = blockNumber.subtract(BLOCKS);
			if (CURRENT_SYNC_BLOCK_NUMBER.compareTo(TRC20_TARGET_BLOCK_NUMBER) <= 0) {
				String transactionInfoByBlockNum = tronservice.gettransactioninfobyblocknum(CURRENT_SYNC_BLOCK_NUMBER);
				JSONArray parseArray = JSONUtil.parseArray(transactionInfoByBlockNum);
				log.info("当前同步TRC20区块:" + CURRENT_SYNC_BLOCK_NUMBER + ",txs:" + parseArray.size());
				if (parseArray.size() > 0) {
					parseArray.forEach(e -> {
						try {
							String txId = JSONUtil.parseObj(e.toString()).getStr("id");
							Tronmemberorder tronmemberorder = tronmemberordermapper.getByTxId(txId);
							long beginTime1 = System.currentTimeMillis();
							long time1 = System.currentTimeMillis() - beginTime1;
							log.info(">>>>>>>>>>>>>>>>>>>> 处理时间  Time = {} /ms", time1);
							if (tronmemberorder == null) {
								JSONObject parseObject = JSONUtil.parseObj(tronservice.gettransactionbyid(txId));
								if (parseObject != null && parseObject.getJSONArray("ret") != null) {
									String contractRet = parseObject.getJSONArray("ret").getJSONObject(0).getStr("contractRet");
									// log.info("contractRet:" + contractRet +" txid="+ txId);
									// 交易成功
									if ("SUCCESS".equalsIgnoreCase(contractRet)) {
										String type = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getStr("type");
										if ("TriggerSmartContract".equalsIgnoreCase(type)) {
											// 合约地址转账
											triggerSmartContract(parseObject, txId);
										} else if ("TransferContract".equalsIgnoreCase(type)) {
											// trx 转账
											transferContract(parseObject);
										}
									}
								}
							}
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					});
				}
				CURRENT_SYNC_BLOCK_NUMBER = CURRENT_SYNC_BLOCK_NUMBER.add(BigInteger.ONE);
				log.info("blockNumber:" + CURRENT_SYNC_BLOCK_NUMBER + " ==" + TRC20_TARGET_BLOCK_NUMBER);
				charge();
			}
		} else {
			init();
		}
	}

	/**
	 * 扫描TRX交易
	 *
	 * @throws Throwable
	 */
	private synchronized void transferContract(JSONObject parseObject) throws Throwable {
		JSONObject jsonObject = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value");
		// 调用者地址
		String ownerAddress = jsonObject.getStr("owner_address");

		String amount = jsonObject.getStr("amount");

		// 转入地址
		String toAddress = jsonObject.getStr("to_address");

		if (toAddress.equalsIgnoreCase(owneraddress)) {
			log.info("+++++++++++++++++++++++++++++++++" + ownerAddress + "  toaddress" + toAddress + "  amount" + amount);
			// 查看USDT余额是否大于0
			BigDecimal bigDecimal = balanceOfTrc20(toAddress, contract);
			if (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
				// 确定用户充值，归集
				// String hash = sendTrc20(address, bigDecimal, ownerAddress, privatekey);
				// log.info("给 " + toAddress + "转账，交易hash：" + hash);
			}
		}
	}

	/**
	 * 发起trc20转账 (目标地址,数量,合约地址,私钥) 地址 默认为usdt 合约地址
	 * 
	 * @throws Throwable
	 */
	@SuppressWarnings("rawtypes")
	public synchronized String sendTrc20(String toAddress, BigDecimal amount, String ownerAddress, String privateKey) throws Throwable {

		List<Type> inputParameters = new ArrayList<>();
		inputParameters.add(new Address(TronUtil.toHexAddress(toAddress).substring(2)));
		inputParameters.add(new Uint256(amount.multiply(decimal).toBigInteger()));
		String parameter = FunctionEncoder.encodeConstructor(inputParameters);

		String responseString = tronservice.triggersmartcontract(privatekey, ownerAddress, contract, parameter, 6000000L, 0);
		System.out.println("trc20 result:" + responseString);
		return responseString;
	}

	/**
	 * 扫描合约地址交易
	 *
	 * @throws Throwable
	 */
	private synchronized void triggerSmartContract(JSONObject parseObject, String txId) throws Throwable {
		// 方法参数
		JSONObject jsonObject = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value");
		// 合约地址
		String contractAddress = jsonObject.getStr("contract_address");
		String owner_address = jsonObject.getStr("owner_address");
		String data = jsonObject.getStr("data").substring(8);

		List<String> strList = TransformUtil.getStrList(data, 64);
		if (strList.size() != 2) {
			return;
		}
		String toAddress = TransformUtil.delZeroForNum(strList.get(0));

		// 收款地址
		// 相匹配的合约地址
		if (!contract.equalsIgnoreCase(contractAddress)) {
			return;
		}

		String amountStr = TransformUtil.delZeroForNum(strList.get(1));

		if (amountStr.length() > 0) {
			amountStr = new BigInteger(amountStr, 16).toString(10);
		}
		// amount 是充币的数量，自己操作
		BigDecimal amount = BigDecimal.ZERO;
		if (StringUtils.isNotEmpty(amountStr)) {
			amount = new BigDecimal(amountStr).divide(decimal, 6, RoundingMode.FLOOR);
		}

		// 判斷是否张转给自己
		if (toAddress.equalsIgnoreCase(owneraddress)) {
			log.info("============================" + contractAddress + "  owner_address" + owner_address + "  amount" + amount);
			// 具体流程操作
			// 打TRX手续费,以便归集时使用。
			// String hash = sendTrx(new BigDecimal("2.5"), toAddress);
			// log.info("给 " + toAddress + "发送手续费，交易hash：" + hash);
		}
	}

	/**
	 * 查询trc20数量
	 */
	@SuppressWarnings("rawtypes")
	public BigDecimal balanceOfTrc20(String address, String contract) {
		List<Type> inputParameters = new ArrayList<Type>();
		inputParameters.add(new Address(TronUtil.toHexAddress(address).substring(2)));
		// 调用常量合约 交易不上区块链
		String responseString = tronservice.triggerconstantcontract(TronUtil.toHexAddress(address), TronUtil.toHexAddress(contract), FunctionEncoder.encodeConstructor(inputParameters));
		if (StringUtils.isNotEmpty(responseString)) {
			JSONObject obj = JSONUtil.parseObj(responseString);
			JSONArray results = obj.getJSONArray("constant_result");
			if (results != null && results.size() > 0) {
				BigInteger amount = new BigInteger(results.getStr(0), 16);
				return new BigDecimal(amount).divide(decimal, 6, RoundingMode.FLOOR);
			}
		}
		return BigDecimal.ZERO;
	}

	/**
	 * TRX转账
	 */
	public String sendTrx(BigDecimal amount, String toAddress) throws Throwable {
		String privateKey = "私钥";
		String result = tronservice.createtransaction(privateKey, toAddress, owneraddress, amount.multiply(decimal).toBigInteger());
		if (StringUtils.isNotEmpty(result)) {
			return result;
		}
		return null;

	}

	/**
	 * 查询TRX余额
	 */
	public BigDecimal balanceOf(String address) {
		String result = tronservice.getaccount(address);
		BigInteger balance = BigInteger.ZERO;
		if (!StringUtils.isEmpty(result)) {
			JSONObject obj = JSONUtil.parseObj(result);
			BigInteger b = obj.getBigInteger("balance");
			if (b != null) {
				balance = b;
			}
		}
		log.info("trx余额：" + balance);
		return new BigDecimal(balance).divide(decimal, 6, RoundingMode.FLOOR);
	}

}
