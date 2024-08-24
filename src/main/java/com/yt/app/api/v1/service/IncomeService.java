package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.dbo.QrcodeSubmitDTO;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.QrcodeResultVO;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */

public interface IncomeService extends YtIBaseService<Income, Long> {

	Income getByOrderNum(String ordernum);

	YtIPage<IncomeVO> page(Map<String, Object> param);

	// 拉码下单查单
	QrcodeResultVO submitQrcode(QrcodeSubmitDTO qs);

	// 拉码下单查单
	QueryQrcodeResultVO queryqrcode(QrcodeSubmitDTO qs);
}