package com.yt.app.api.v1.service;

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

	Income getByOrderNum(String ordernum);

	YtIPage<IncomeVO> page(Map<String, Object> param);

	// h5上家下单
	QrcodeResultVO submitInCome(QrcodeSubmitDTO qs);

	// h5上家查单
	QueryQrcodeResultVO queryInCome(QrcodeSubmitDTO qs);

	// 拉码下单查单
	QrcodeResultVO submitQrcode(QrcodeSubmitDTO qs);

	// KF回调
	void kfcallback(Map<String, String> params);

	// yjj回调
	void yjjcallback(Map<String, String> params);

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

	// alipay回調
	void alipayftfcallback(Map<String, String> params);

	// 补单
	Integer makeuporder(Income income);

	// 通知
	Integer notify(Income income);
}