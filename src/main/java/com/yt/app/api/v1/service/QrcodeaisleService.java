package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.vo.QrcodeaisleVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */

public interface QrcodeaisleService extends YtIBaseService<Qrcodeaisle, Long> {
	YtIPage<QrcodeaisleVO> page(Map<String, Object> param);
}