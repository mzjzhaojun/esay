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
	YtIPage<TronVO> page(Map<String, Object> param);

	// 验证地址
	boolean validateaddress(String address);

	// 创建账户
	void createaccount(String owneraddress, String toaddress);

	// 获取账户
	void getaccount(String address);

	// 更新账户名称
	void updateaccount(String name, String address);

	// 获取区块账户余额
	void getaccountbalance(String address, Integer block);

	// 设置账号id
	void setaccountid(String address, Long accountid);

	// 通过账户id查询账户
	void getaccountbyid(Long accountid);

	// 创建交易
	void createtransaction(String privatekey, String toaddress, String owneraddress, Integer amount);

	// 广播交易
	void broadcasttransaction(HashMap<String, Object> map);

	// 广播签名交易后的字符串
	void broadcasthex(String address);

	// 查询签名交易后的总权重
	void getsignweight(String signature, String txid);

	// 查询交易签名后的签名列表
	void getapprovedlist(String signature, String txid);

	// 查询账户资源
	void getaccountresource(String address);

	// 查询账户宽带信息
	void getaccountnet(String address);

	// 质押trx
	void freezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource);

	// 解锁TRX
	void unfreezebalancev2(String privatekey, String owneraddress, BigInteger frozenbalance, String resource);

	// 取消质押
	void cancelallunfreezev2(String privatekey, String owneraddress);

	// 将带宽或者能量资源代理给其它账户
	void delegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource, boolean lock, Integer lockperiod);

	// 取消为目标地址代理的带宽或者能量
	void undelegateresource(String privatekey, String owneraddress, String receiveraddress, BigInteger balance, String resource);

	// 查询最新区块信息
	void getnowblock();

	// 查询区块根据id和hash
	void getblock(String idornum);

	// 根据区块高度 查询快
	void getblockbynum(Integer num);

	// 根据区块id 查询快
	void getblockbyid(String value);

	// 查询最新的num几个区块
	void getblockbylatestnum(Integer num);

	// 查询区块余额
	void getblockbalance(String hash, Integer number);

	// 通过交易id查询某笔交易的信息
	void gettransactionbyid(String txid);

	// 根据交易id查询交易费用，区块高度
	void gettransactioninfobyid(String txid);

	// 查询特定区块中的交易数量
	void gettransactioncountbyblocknum(Integer number);

	// 查询特定区块高度交易信息列表
	void gettransactioninfobyblocknum(Integer number);

	// 查询当前节点信息
	void getnodeinfo();
}