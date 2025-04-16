package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodpaymember;
import com.yt.app.api.v1.vo.QrcodpaymemberVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-16 23:44:12
 */

public interface QrcodpaymemberService extends YtIBaseService<Qrcodpaymember, Long> {
	YtIPage<QrcodpaymemberVO> page(Map<String, Object> param);
}