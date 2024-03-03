package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.vo.AislechannelVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:16:34
 */

public interface AislechannelService extends YtIBaseService<Aislechannel, Long> {
	YtIPage<AislechannelVO> page(Map<String, Object> param);
}