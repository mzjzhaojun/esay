package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */

public interface IncomemerchantaccountService extends YtIBaseService<Incomemerchantaccount, Long> {
	YtIPage<IncomemerchantaccountVO> page(Map<String, Object> param);

	/**
	 * 代收待确认
	 * 
	 * @param t
	 */
	void totalincome(Incomemerchantaccountorder t);

	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Incomemerchantaccountorder mao);

	/**
	 * 超时取消
	 * 
	 * @param mao
	 */
	void cancleTotalincome(Incomemerchantaccountorder mao);

	/**
	 * 
	 * @return
	 */
	Incomemerchantaccount getData();

	/**
	 * 
	 * @param id
	 * @return
	 */
	Incomemerchantaccount getDataBank(Long id);

	/**
	 * 代收提现
	 * 
	 * @param t
	 */
	void withdrawamount(Incomemerchantaccountorder t);

	/**
	 * 代收提现成功
	 * 
	 * @param t
	 */
	void updateWithdrawamount(Incomemerchantaccountorder mao);

	/**
	 * 代收提现失败
	 * 
	 * @param t
	 */
	void turndownWithdrawamount(Incomemerchantaccountorder mao);

	/**
	 * 取消
	 * 
	 * @param t
	 */
	void cancelWithdrawamount(Incomemerchantaccountorder mao);

}