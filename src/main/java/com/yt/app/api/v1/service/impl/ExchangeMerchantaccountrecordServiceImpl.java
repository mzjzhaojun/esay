package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ExchangeMerchantaccountrecordMapper;
import com.yt.app.api.v1.service.ExchangeMerchantaccountrecordService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountrecord;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class ExchangeMerchantaccountrecordServiceImpl extends YtBaseServiceImpl<ExchangeMerchantaccountrecord, Long>
		implements ExchangeMerchantaccountrecordService {
	@Autowired
	private ExchangeMerchantaccountrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(ExchangeMerchantaccountrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<ExchangeMerchantaccountrecord> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<ExchangeMerchantaccountrecord>(Collections.emptyList());
			}
		}
		List<ExchangeMerchantaccountrecord> list = mapper.list(param);
		list.forEach(maaj -> {
			maaj.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + maaj.getType()));
		});
		return new YtPageBean<ExchangeMerchantaccountrecord>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ExchangeMerchantaccountrecord get(Long id) {
		ExchangeMerchantaccountrecord t = mapper.get(id);
		return t;
	}

}