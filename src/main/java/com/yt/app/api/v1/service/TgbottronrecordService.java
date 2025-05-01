package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgbottronrecord;
import com.yt.app.api.v1.vo.TgbottronrecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-05-01 14:00:37
 */

public interface TgbottronrecordService extends YtIBaseService<Tgbottronrecord, Long> {
	YtIPage<TgbottronrecordVO> page(Map<String, Object> param);
}