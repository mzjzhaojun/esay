package com.yt.app.api.v1.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.dbo.SysQueryDTO;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

public interface PayoutService extends YtIBaseService<Payout, Long> {

	// 代付盘口下单查单
	PayResultVO query(SysQueryDTO squery);

	// 查询余额
	PayResultVO queryblance(String merchantid);

	// 盘口提交订单
	PayResultVO submit(PaySubmitDTO ss);

	// 分頁查詢
	YtIPage<PayoutVO> page(Map<String, Object> param);

	// 本地提交订单
	Integer submitOrder(Payout pt);

	// 本地自营渠道
	Integer submitOrderSelf(Payout pt);

	// 天下回調
	YtBody txcallbackpay(SysTyOrder so);

	// 天下回調
	YtBody exist(SysTyOrder so);

	// 盛世回调
	void sscallback(Map<String, Object> params);

	// 易生回调
	void ysdfcallback(Map<String, String> params);

	// 十年回调
	void sncallback(Map<String, Object> params);

	// 旭日代付回调
	void xrcallback(Map<String, String> params);

	// 旭日代付回调
	void sxcallback(Map<String, String> params);

	// 旭日代付回调
	void ljcallback(Map<String, String> params);

	// HYT代付回调
	void hytcallback(Map<String, String> params);

	// 仙剑代付回调
	void xjcallback(Map<String, Object> params);

	// 8g代付回调
	void g8callback(Map<String, Object> params);

	// 环宇代付回调
	void hycallback(String orderid);

	// 青蛙代付回调
	void qwcallback(Map<String, String> params);

	// 通银代付回调
	void tycallback(Map<String, String> params);

	// 飞兔代付回调
	void ftcallback(Map<String, String> params);

	// 易票联代付回调
	void epfcallback(Map<String, Object> params);

	// 导入代付渠道
	String upFile(MultipartFile file, String aisleid) throws IOException;

	// 导入代付自营
	String uploadself(MultipartFile file, String aisleid) throws IOException;

	// 成功
	Integer success(Long id);

	// 失敗
	Integer fail(Long id);

	// 回单
	Integer handleCertificate(Long id);

	// 下载回单
	Integer handleCertificateDownload(Long id);

	// 下载
	ByteArrayOutputStream download(Map<String, Object> param) throws IOException;
}