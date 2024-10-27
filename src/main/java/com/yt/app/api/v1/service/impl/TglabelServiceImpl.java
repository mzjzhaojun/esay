package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgchannelgrouplabelMapper;
import com.yt.app.api.v1.service.TglabelService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tgchannelgrouplabel;
import com.yt.app.api.v1.vo.TgchannelgrouplabelVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */

@Service
public class TglabelServiceImpl extends YtBaseServiceImpl<Tgchannelgrouplabel, Long> implements TglabelService {
	@Autowired
	private TgchannelgrouplabelMapper mapper;

	@Override
	@Transactional
	public Integer post(Tgchannelgrouplabel t) {
		Integer i = mapper.post(t);
		return i;
	}


	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tgchannelgrouplabel get(Long id) {
		Tgchannelgrouplabel t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TgchannelgrouplabelVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgchannelgrouplabelVO>(Collections.emptyList());
		}
		List<TgchannelgrouplabelVO> list = mapper.page(param);
		return new YtPageBean<TgchannelgrouplabelVO>(param, list, count);
	}
}