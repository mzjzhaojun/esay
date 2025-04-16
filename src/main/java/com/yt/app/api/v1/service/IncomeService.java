package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */

public interface IncomeService extends YtIBaseService<Income, Long> {

	List<Income> list();

	Income getByOrderNum(String ordernum);

	YtIPage<IncomeVO> page(Map<String, Object> param);

	// h5上家下单
	QrcodeResultVO submitInCome(QrcodeSubmitDTO qs);

	// h5上家查单
	QueryQrcodeResultVO queryInCome(QrcodeSubmitDTO qs);

	// KF回调
	void kfcallback(Map<String, String> params);

	// 二狗
	void egcallback(Map<String, String> params);

	// 豌豆回调
	void wdcallback(Map<String, String> params);

	// 玩家回调
	void wjcallback(Map<String, String> params);

	// 日不落回调
	void rblcallback(Map<String, Object> params);

	// 翡翠回调
	void fccallback(Map<String, Object> params);

	// 翡翠回调
	void aklcallback(Map<String, Object> params);

	// 公子回调
	void gzcallback(Map<String, String> params);

	// 飞黄运通回调
	void fhcallback(Map<String, String> params);

	// 易生回调
	void yscallback(Map<String, String> params);

	// 新生回调
	void xscallback(Map<String, String> params);

	// 张三
	void zscallback(Map<String, Object> params);

	// alipay回調
	void alipayftfcallback(Map<String, String> params);

	// 通源回調
	void tongyuancallback(Map<String, String> params);

	// 通知
	Integer notify(Income income);

	// 成功
	void successstatus(Income income);

	// 拉黑
	Integer addblock(Income income);

	// 确认结算
	Integer settleconfirm(Income income);

	// 易票联绑卡
	void sumbmitcheck(Map<String, Object> params);

	// 易票联支付
	void sumbmitcpay(Map<String, Object> params);

	// 易票联回调
	void epfpayftfcallback(Map<String, Object> params);

}