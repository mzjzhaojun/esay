package com.yt.app.api.v1.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.vo.MerchantaccountorderVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

public interface MerchantaccountorderService extends YtIBaseService<Merchantaccountorder, Long> {

	YtIPage<MerchantaccountorderVO> page(Map<String, Object> param);

	ByteArrayOutputStream downloadIncome(Map<String, Object> param) throws IOException;

	ByteArrayOutputStream downloadPayout(Map<String, Object> param) throws IOException;

	// 充值成功
	Integer incomemanual(Merchantaccountorder mco);

	// 取消充值
	Integer incomecancle(Long id);

	// 代付提现
	Integer payoutwithdraw(Merchantaccountorder mco);

	// 提现成功
	Integer payoutmanual(Merchantaccountorder mco);

	// 提现取消
	Integer payoutcancleWithdraw(Long id);

	// app提现
	Integer incomewithdrawapp(Merchantaccountorder mco);

	// 代收提现
	Integer incomewithdraw(Merchantaccountorder mco);

	// 代收提现成功
	Integer incomewithdrawmanual(Merchantaccountorder mco);

	// 代收提现取消
	Integer cancleincomewithdraw(Long id);

}