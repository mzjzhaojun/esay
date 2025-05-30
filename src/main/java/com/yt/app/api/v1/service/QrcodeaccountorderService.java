package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:07:27
 */

public interface QrcodeaccountorderService extends YtIBaseService<Qrcodeaccountorder, Long> {
	YtIPage<QrcodeaccountorderVO> page(Map<String, Object> param);

	void incomewithdrawTelegram(Channel m, double amount);

}