package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.vo.TgmessagegroupVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

public interface TgmessagegroupService extends YtIBaseService<Tgmessagegroup, Long> {
	YtIPage<TgmessagegroupVO> page(Map<String, Object> param);

	Integer putmerchant(Tgmessagegroup t);
}