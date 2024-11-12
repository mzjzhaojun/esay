package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.TgmessagegroupMapper;
import com.yt.app.api.v1.service.TgmessagegroupService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.vo.TgmessagegroupVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

@Service
public class TgmessagegroupServiceImpl extends YtBaseServiceImpl<Tgmessagegroup, Long> implements TgmessagegroupService {
	@Autowired
	private TgmessagegroupMapper mapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Override
	@Transactional
	public Integer post(Tgmessagegroup t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgmessagegroup get(Long id) {
		Tgmessagegroup t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TgmessagegroupVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgmessagegroupVO>(Collections.emptyList());
		}
		List<TgmessagegroupVO> list = mapper.page(param);
		list.forEach(e -> {
			List<Long> ids = e.getMerchantids();
			if (ids != null) {
				StringBuffer sb = new StringBuffer();
				ids.forEach(id -> {
					Merchant merchant = merchantmapper.get(id);
					sb.append(merchant.getName() + " ");
				});
				e.setMerchantname(sb.toString());
			}
		});
		return new YtPageBean<TgmessagegroupVO>(param, list, count);
	}

	@Override
	@Transactional
	public Integer putmerchant(Tgmessagegroup t) {
		return mapper.put(t);
	}
}