package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Incomemerchantaccountrecord;
import com.yt.app.api.v1.vo.IncomemerchantaccountrecordVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

public interface IncomemerchantaccountrecordService extends YtIBaseService<Incomemerchantaccountrecord, Long> {
	YtIPage<IncomemerchantaccountrecordVO> page(Map<String, Object> param);
}