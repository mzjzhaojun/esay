package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.QrcodeaisleqrcodeVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */

public interface QrcodeaisleqrcodeService extends YtIBaseService<Qrcodeaisleqrcode, Long> {
	YtIPage<QrcodeaisleqrcodeVO> page(Map<String, Object> param);
}