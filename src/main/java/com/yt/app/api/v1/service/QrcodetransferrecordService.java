package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodetransferrecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;
/**
* @author zj default
* 
* @version v1
* @createdate2025-04-13 22:38:13
*/

public interface QrcodetransferrecordService extends YtIBaseService<Qrcodetransferrecord, Long>{
YtIPage<QrcodetransferrecordVO> page(Map<String, Object> param);
}