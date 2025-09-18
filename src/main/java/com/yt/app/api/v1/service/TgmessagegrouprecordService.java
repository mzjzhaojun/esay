package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmessagegrouprecord;
import com.yt.app.api.v1.vo.TgmessagegrouprecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-09-19 01:40:22
 */

public interface TgmessagegrouprecordService extends YtIBaseService<Tgmessagegrouprecord, Long> {
	YtIPage<TgmessagegrouprecordVO> page(Map<String, Object> param);
}