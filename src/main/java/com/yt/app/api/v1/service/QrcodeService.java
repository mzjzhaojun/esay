package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
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
	QrcodeVO paytest(Qrcode qv);
	
	/**
	 * 代收成功
	 * 
	 * @param mao
	 */
	void updateTotalincome(Qrcodeaccountorder mao);
}