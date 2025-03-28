package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Blocklist;
import com.yt.app.api.v1.vo.BlocklistVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 14:56:50
 */

public interface BlocklistService extends YtIBaseService<Blocklist, Long> {

	YtIPage<BlocklistVO> page(Map<String, Object> param);

	Blocklist getByHexaddress(String hexaddress, Long orderid);
}