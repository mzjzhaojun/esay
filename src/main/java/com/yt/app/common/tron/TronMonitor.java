package com.yt.app.common.tron;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.mapper.TronmemberMapper;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.common.bot.TronBot;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.TransformUtil;
import com.yt.app.common.util.TronUtils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.RoundingMode;

//@Slf4j
//@Profile("dev")
//@Component
public class TronMonitor {

	@Autowired
	private TronBot tronbot;

	@Autowired
	private TronService tronservice;

	@Autowired
	private TronmemberMapper tronmembermapper;

	@Autowired
	private TronmemberorderMapper tronmemberordermapper;

	private BigDecimal decimal = new BigDecimal("1000000");

	private String contract = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";

//	private String collecaddress = "TWXQjegKptQkfaGXA3m7V5A2AnMGT88888";

	private static BigInteger TRC20_TARGET_BLOCK_NUMBER;
	private static BigInteger CURRENT_SYNC_BLOCK_NUMBER;
	private static BigInteger BLOCKS = new BigInteger("5");

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		if (TRC20_TARGET_BLOCK_NUMBER == null) {
			String responseString = TronUtils.getnowblock();
			JSONObject jsonObject = JSONUtil.parseObj(responseString);
			BigInteger blockNumber = jsonObject.getJSONObject("block_header").getJSONObject("raw_data").getBigInteger("number");
			CURRENT_SYNC_BLOCK_NUMBER = blockNumber.subtract(BLOCKS);
		}
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void synTrxBalance() throws Throwable {
		Tron tron = tronservice.get();
		Double balance = Double.valueOf(balanceOf(tron.getAddress()).toString());
		if (balance >= 510) {
//			sendTrx(new BigDecimal(balance - 500), collecaddress, tron.getAddress(), tron.getPrivatekey());
		}
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void synTrcBalance() throws Throwable {
//		Tron tron = tronservice.get();
//		Double balance = Double.valueOf(balanceOfTrc20(tron.getAddress(), contract).toString());
//		if (balance >= 100) {
//			sendTrc20(collecaddress, new BigDecimal(balance - 99), tron.getAddress(), tron.getPrivatekey());
//		}
	}

	/**
	 * 归集，自定义执行时间
	 *
	 * @throws Throwable
	 */
	@Scheduled(cron = "0/15 * * * * ?")
	public void charge() throws Throwable {
		if (CURRENT_SYNC_BLOCK_NUMBER != null) {
			Tron tron = tronservice.get();
			String responseString = TronUtils.getnowblock();
			JSONObject jsonObject = JSONUtil.parseObj(responseString);
			BigInteger blockNumber = jsonObject.getJSONObject("block_header").getJSONObject("raw_data").getBigInteger("number");
			TRC20_TARGET_BLOCK_NUMBER = blockNumber.subtract(BLOCKS);
			if (CURRENT_SYNC_BLOCK_NUMBER.compareTo(TRC20_TARGET_BLOCK_NUMBER) <= 0) {
				String transactionInfoByBlockNum = TronUtils.gettransactioninfobyblocknum(CURRENT_SYNC_BLOCK_NUMBER);
				JSONArray parseArray = JSONUtil.parseArray(transactionInfoByBlockNum);
				if (parseArray.size() > 0) {
					parseArray.forEach(e -> {
						try {
							String txId = JSONUtil.parseObj(e.toString()).getStr("id");
							JSONObject parseObject = JSONUtil.parseObj(TronUtils.gettransactionbyid(txId));
							if (parseObject != null && parseObject.getJSONArray("ret") != null) {
								String contractRet = parseObject.getJSONArray("ret").getJSONObject(0).getStr("contractRet");
								// 交易成功
								if ("SUCCESS".equalsIgnoreCase(contractRet)) {
									String type = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getStr("type");
									if ("TriggerSmartContract".equalsIgnoreCase(type)) {
										// 合约地址转账
										triggerSmartContract(parseObject, txId, tron.getAddress(), tron.getPrivatekey());
									} else if ("TransferContract".equalsIgnoreCase(type)) {
										// trx 转账
										transferContract(parseObject, tron.getAddress(), tron.getPrivatekey());
									}
								}
							}
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					});
				}
				CURRENT_SYNC_BLOCK_NUMBER = CURRENT_SYNC_BLOCK_NUMBER.add(BigInteger.ONE);
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
	private synchronized void transferContract(JSONObject parseObject, String owneraddress, String privatekey) throws Throwable {
		JSONObject jsonObject = parseObject.getJSONObject("raw_data").getJSONArray("contract").getJSONObject(0).getJSONObject("parameter").getJSONObject("value");
		// 转入地址
		String toAddress = jsonObject.getStr("to_address");
		if (toAddress.equalsIgnoreCase(owneraddress)) {
			Double balance = Double.valueOf(balanceOf(owneraddress).toString());
			if (balance >= 1200) {
//				sendTrx(new BigDecimal(balance - 1200), collecaddress, owneraddress, privatekey);
			}
		}
	}

	/**
	 * 扫描合约地址交易
	 *
	 * @throws Throwable
	 */
	private synchronized void triggerSmartContract(JSONObject parseObject, String txId, String owneraddress, String privatekey) throws Throwable {
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
			Tronmemberorder tronmemberorder = tronmemberordermapper.getByTxId(txId);
			if (tronmemberorder == null) {
				tronmemberorder = tronmemberordermapper.getByAmount(Double.valueOf(amount.toString()));
				if (tronmemberorder != null) {
					if (tronmemberorder.getType().equals(DictionaryResource.EXCHANGE_TYPE_601)) {
//						String hash = sendTrx(new BigDecimal(tronmemberorder.getTrxamount().toString()), owner_address, owneraddress, privatekey);
//						log.info("给 " + toAddress + "发送手续费，交易hash：" + hash);
						tronbot.notifyMermberSuccess(tronmemberorder.getTgid(), tronmemberorder.getOrdernum(), tronmemberorder.getAmount());
					} else if (tronmemberorder.getType().equals(DictionaryResource.EXCHANGE_TYPE_602)) {
						// 處理充值
						Tronmember tronmember = tronmembermapper.getByTgId(tronmemberorder.getTgid());
						tronmember.setUsdtbalance(tronmember.getUsdtbalance() + Double.valueOf(amount.toString()));
						tronmembermapper.put(tronmember);
						tronbot.notifyMermberIncomeSuccess(tronmemberorder.getTgid(), tronmemberorder.getOrdernum(), tronmemberorder.getAmount());
					}
					tronmemberorder.setStatus(DictionaryResource.PAYOUTSTATUS_52);
					tronmemberorder.setFromaddress(owner_address);
					tronmemberorder.setTxid(txId);
					tronmemberorder.setSuccessdate(DateTimeUtil.getNow());
					tronmemberordermapper.put(tronmemberorder);
					// 刪除緩存數據
					NumberUtil.removeExchangeFewAmount(tronmemberorder.getFewamount());
				}
			}
		}
	}

	/**
	 * 查询TRX余额
	 */
	public BigDecimal balanceOf(String address) {
		String result = TronUtils.getaccount(address);
		BigInteger balance = BigInteger.ZERO;
		if (!StringUtils.isEmpty(result)) {
			JSONObject obj = JSONUtil.parseObj(result);
			BigInteger b = obj.getBigInteger("balance");
			if (b != null) {
				balance = b;
			}
		}
		return new BigDecimal(balance).divide(decimal, 6, RoundingMode.FLOOR);
	}

}
