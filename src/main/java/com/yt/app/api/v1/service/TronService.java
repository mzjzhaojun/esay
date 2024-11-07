package com.yt.app.api.v1.service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 15:25:57
 */

public interface TronService extends YtIBaseService<Tron, Long> {

	Tron get();

	YtIPage<TronVO> page(Map<String, Object> param);

	//
	String getchainparameters();

	// 验证地址
	boolean validateaddress(String address);

	// 创建账户
	String createaccount(String owneraddress, String toaddress);

	// 获取账户
	String getaccount(String address);

	// 更新账户名称
	String updateaccount(String name, String address);

	// 获取区块账户余额
	String getaccountbalance(String address, Integer block);

	// 设置账号id
	String setaccountid(String address, Long accountid);

	// 通过账户id查询账户
	String getaccountbyid(Long accountid);

	// 创建交易
	String createtransaction(String privatekey, String toaddress, String owneraddress, BigInteger amount);

	// 广播交易
	String broadcasttransaction(HashMap<String, Object> map);

	// 广播签名交易后的字符串
	String broadcasthex(HashMap<String, Object> map);

	// 查询签名交易后的总权重
	String getsignweight(String signature, String txid);

	// 查询交易签名后的签名列表
	String getapprovedlist(String signature, String txid);

	// 查询账户资源
	String getaccountresource(String address);

	// 查询账户宽带信息
	String getaccountnet(String address);

	// 质押trx
	String freezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource);

	// 解锁TRX
	String unfreezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource);

	// 取消质押
	String cancelallunfreezev2(String privatekey, String owneraddress);

	// 将带宽或者能量资源代理给其它账户
	String delegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource, boolean lock, Integer lockperiod);

	// 取消为目标地址代理的带宽或者能量
	String undelegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource);

	// 查询最新区块信息
	String getnowblock();

	// 查询区块根据id和hash
	String getblock(String idornum);

	// 根据区块高度 查询快
	String getblockbynum(BigInteger num);

	// 根据区块id 查询快
	String getblockbyid(String value);

	// 查询最新的num几个区块
	String getblockbylatestnum(Integer num);

	// 查询区块余额
	String getblockbalance(String hash, Integer number);

	// 通过交易id查询某笔交易的信息
	String gettransactionbyid(String txid);

	// 根据交易id查询交易费用，区块高度
	String gettransactioninfobyid(String txid);

	// 查询特定区块中的交易数量
	String gettransactioncountbyblocknum(Integer number);

	// 查询特定区块高度交易信息列表
	String gettransactioninfobyblocknum(BigInteger number);

	// 查询当前节点信息
	String getnodeinfo();

	// 获取合约
	String getcontract(String address);

	// 调用合约
	String triggersmartcontract(String privatekey, String owner_address, String contract_address, String parameter, long fee_limit, Integer call_value);

	// 调用常量合约，产生的交易不上链
	String triggerconstantcontract(String owner_address, String contract_address, String parameter);

}