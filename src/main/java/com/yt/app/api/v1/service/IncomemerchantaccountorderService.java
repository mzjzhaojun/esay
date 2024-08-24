package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

public interface IncomemerchantaccountorderService extends YtIBaseService<Incomemerchantaccountorder, Long> {
	YtIPage<IncomemerchantaccountorderVO> page(Map<String, Object> param);
}