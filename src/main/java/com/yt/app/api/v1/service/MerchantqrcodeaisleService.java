package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.vo.MerchantqrcodeaisleVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 16:58:38
 */

public interface MerchantqrcodeaisleService extends YtIBaseService<Merchantqrcodeaisle, Long> {
	YtIPage<MerchantqrcodeaisleVO> page(Map<String, Object> param);
}