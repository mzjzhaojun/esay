package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.vo.TronmemberorderVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-10-15 00:23:49
 */

public interface TronmemberorderService extends YtIBaseService<Tronmemberorder, Long> {
	YtIPage<TronmemberorderVO> page(Map<String, Object> param);
}