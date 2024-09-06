package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tronaddress;
import com.yt.app.api.v1.vo.TronaddressVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 01:44:43
 */

public interface TronaddressService extends YtIBaseService<Tronaddress, Long> {
	YtIPage<TronaddressVO> page(Map<String, Object> param);

}