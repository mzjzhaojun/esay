package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodeaccountrecord;
import com.yt.app.api.v1.vo.QrcodeaccountrecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

public interface QrcodeaccountrecordService extends YtIBaseService<Qrcodeaccountrecord, Long> {
	YtIPage<QrcodeaccountrecordVO> page(Map<String, Object> param);
}