package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TglabelMapper;
import com.yt.app.api.v1.service.TglabelService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tglabel;
import com.yt.app.api.v1.vo.TglabelVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */

@Service
public class TglabelServiceImpl extends YtBaseServiceImpl<Tglabel, Long> implements TglabelService {
	@Autowired
	private TglabelMapper mapper;

	@Override
	@Transactional
	public Integer post(Tglabel t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Tglabel> list(Map<String, Object> param) {
		List<Tglabel> list = mapper.list(param);
		return new YtPageBean<Tglabel>(list);
	}

	@Override
	public Tglabel get(Long id) {
		Tglabel t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TglabelVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TglabelVO>(Collections.emptyList());
		}
		List<TglabelVO> list = mapper.page(param);
		return new YtPageBean<TglabelVO>(param, list, count);
	}
}