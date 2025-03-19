package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodestatisticalreports;
import com.yt.app.api.v1.vo.QrcodestatisticalreportsVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;
/**
* @author zj default
* 
* @version v1
* @createdate2025-03-19 19:51:13
*/

public interface QrcodestatisticalreportsService extends YtIBaseService<Qrcodestatisticalreports, Long>{
YtIPage<QrcodestatisticalreportsVO> page(Map<String, Object> param);
}