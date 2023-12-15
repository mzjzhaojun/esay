package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.DictMapper;
import com.yt.app.api.v1.mapper.DicttypeMapper;
import com.yt.app.api.v1.service.DicttypeService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Dicttype;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@Service
public class DicttypeServiceImpl extends YtBaseServiceImpl<Dicttype, Long> implements DicttypeService {
	@Autowired
	private DicttypeMapper mapper;

	@Autowired
	private DictMapper dictmapper;

	@Override
	@Transactional
	public Integer post(Dicttype t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Dicttype> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Dicttype> list = mapper.list(param);
		return new YtPageBean<Dicttype>(param, list, count);
	}

	@Override
	public Dicttype get(Long id) {
		Dicttype t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		dictmapper.deletebytypid(id);
		return mapper.delete(id);
	}
}