package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgbotgrouprecord;
import com.yt.app.api.v1.vo.TgbotgrouprecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */

public interface TgbotgrouprecordService extends YtIBaseService<Tgbotgrouprecord, Long> {
	YtIPage<TgbotgrouprecordVO> page(Map<String, Object> param);
}