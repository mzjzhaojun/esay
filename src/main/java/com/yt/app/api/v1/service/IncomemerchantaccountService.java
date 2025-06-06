package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Merchantaccountorder;
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
	void totalincome(Income t);

	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Income mao);

	/**
	 * 超时取消
	 * 
	 * @param mao
	 */
	void cancleTotalincome(Income mao);

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
	void withdrawamount(Merchantaccountorder t);

	/**
	 * 代收提现成功
	 * 
	 * @param t
	 */
	void updateWithdrawamount(Merchantaccountorder t);

	/**
	 * 代收提现取消
	 * 
	 * @param t
	 */
	void cancelWithdrawamount(Merchantaccountorder t);


}