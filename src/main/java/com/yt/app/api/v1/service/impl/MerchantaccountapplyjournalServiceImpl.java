package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantaccountapplyjournalMapper;
import com.yt.app.api.v1.service.MerchantaccountapplyjournalService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantaccountapplyjournal;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class MerchantaccountapplyjournalServiceImpl extends YtBaseServiceImpl<Merchantaccountapplyjournal, Long>
		implements MerchantaccountapplyjournalService {
	@Autowired
	private MerchantaccountapplyjournalMapper mapper;

	@Override
	@Transactional
	public Integer post(Merchantaccountapplyjournal t) {
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Merchantaccountapplyjournal> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Merchantaccountapplyjournal> list = mapper.list(param);
		list.forEach(maaj -> {
			maaj.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + maaj.getType()));
		});
		return new YtPageBean<Merchantaccountapplyjournal>(param, list, count);
	}

	@Override
	public Merchantaccountapplyjournal get(Long id) {
		Merchantaccountapplyjournal t = mapper.get(id);
		return t;
	}

}