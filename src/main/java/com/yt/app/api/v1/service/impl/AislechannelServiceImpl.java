package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.service.AislechannelService;
import com.yt.app.api.v1.vo.AislechannelVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:16:34
 */

@Service
public class AislechannelServiceImpl extends YtBaseServiceImpl<Aislechannel, Long> implements AislechannelService {
	@Autowired
	private AislechannelMapper mapper;

	@Autowired
	private AisleMapper aislemapper;

	@Override
	@Transactional
	public Integer post(Aislechannel t) {
		Aislechannel m = mapper.getByAidCid(t.getAisleid(), t.getChannelid());
		Assert.isNull(m, "已经存在的渠道");
		Integer i = mapper.post(t);
		Aisle as = aislemapper.get(t.getAisleid());
		as.setChannelcount(as.getChannelcount() + 1);
		aislemapper.put(as);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Aislechannel get(Long id) {
		Aislechannel t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Aislechannel t = mapper.get(id);
		Aisle as = aislemapper.get(t.getAisleid());
		as.setChannelcount(as.getChannelcount() - 1);
		aislemapper.put(as);
		return mapper.delete(id);
	}

	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<AislechannelVO> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<AislechannelVO>(Collections.emptyList());
			}
		}
		List<AislechannelVO> list = mapper.page(param);
		return new YtPageBean<AislechannelVO>(param, list, count);
	}
}