package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodeVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

public interface QrcodeService extends YtIBaseService<Qrcode, Long> {

	YtIPage<QrcodeVO> page(Map<String, Object> param);

	/**
	 * 测试拉单
	 * 
	 * @param qv
	 * @return
	 */
	QrcodeVO paytestzft(Qrcode qv);

	/**
	 * 测试拉单
	 * 
	 * @param qv
	 * @return
	 */
	String paytestepl(Map<String, Object> params);

	/**
	 * 易票联支付
	 * 
	 * @param params
	 * @return
	 */
	String paytesteplcafrom(Map<String, Object> params);

	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Income mao);

	/**
	 * 统计
	 * 
	 * @param c
	 * @param date
	 */
	void updateDayValue(Qrcode c, String date);

	/**
	 * 支付通账户余额查询
	 * 
	 * @param c
	 */
	void zftaccountquery(Qrcode c);

	//
	void merchantexpandindirectzftdelete(Qrcode qrcode);

	/**
	 * 易票联账户余额查询
	 * 
	 * @param c
	 */
	void eplaccountquery(Qrcode c);

	/**
	 * 支付宝转账
	 * 
	 * @param c
	 */
	void transunitransfer(Qrcodetransferrecord c);

	/**
	 * 支付宝分账
	 * 
	 * @param c
	 */
	void zfbtradeordersettle(Qrcodetransferrecord c);

	/**
	 * 易票联转账
	 * 
	 * @param c
	 */
	void epltransunitransfer(Qrcodetransferrecord c);

}