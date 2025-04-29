package com.yt.app.api.v1.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

public interface IncomemerchantaccountorderService extends YtIBaseService<Incomemerchantaccountorder, Long> {

	YtIPage<IncomemerchantaccountorderVO> page(Map<String, Object> param);

	ByteArrayOutputStream download(Map<String, Object> param) throws IOException;

	// 充值
	void incomemanual(Incomemerchantaccountorder mco);

	// 代收提现
	Integer incomewithdraw(Incomemerchantaccountorder mco);

	Long incomewithdrawapp(Incomemerchantaccountorder mco);

	Integer success(Long id);

	Integer incomecancleWithdraw(Long id);

	void incomewithdrawmanual(Incomemerchantaccountorder mco);

	void incomewithdrawTelegram(Merchant m, double amount);
}