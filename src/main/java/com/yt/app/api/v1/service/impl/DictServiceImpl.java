package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.DictMapper;
import com.yt.app.api.v1.mapper.DicttypeMapper;
import com.yt.app.api.v1.service.DictService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yt.app.api.v1.entity.Dict;
import com.yt.app.api.v1.entity.Dicttype;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.base.constant.SystemConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@Service
public class DictServiceImpl extends YtBaseServiceImpl<Dict, Long> implements DictService {
	@Autowired
	private DictMapper mapper;

	@Autowired
	private DicttypeMapper dicttypemapper;

	@Override
	@Transactional
	public Integer post(Dict t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Dict> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Dict> list = mapper.list(param);
		return new YtPageBean<Dict>(param, list, count);
	}

	@Override
	public Dict get(Long id) {
		Dict t = mapper.get(id);
		return t;
	}

	@Override
	public List<Dict> listfromcachebycode(String code) {
		String strs = RedisUtil.get(SystemConstant.CACHE_SYS_DICTTYPE_PREFIX + code);
		List<Dict> listd = JSON.parseArray(strs, Dict.class);
		return listd;
	}

	@Override
	public List<Dict> listbycode(Map<String, Object> param) {
		return mapper.list(param);
	}

	@Override
	public void initCache() {
		List<Dicttype> listdicttype = dicttypemapper.list(new HashMap<String, Object>());
		if (!CollectionUtils.isEmpty(listdicttype)) {
			listdicttype.forEach(dcity -> {
				List<Dict> listdict = mapper.listByCode(dcity.getCode());
				RedisUtil.set(SystemConstant.CACHE_SYS_DICTTYPE_PREFIX + dcity.getCode(), JSON.toJSONString(listdict));
				listdict.forEach(d -> {
					RedisUtil.set(SystemConstant.CACHE_SYS_DICT_PREFIX + d.getValue(), d.getName());
				});
			});
		}
	}
}