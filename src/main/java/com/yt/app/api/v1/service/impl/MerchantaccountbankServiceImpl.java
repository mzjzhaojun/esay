package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantaccountbankMapper;
import com.yt.app.api.v1.service.MerchantaccountbankService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.util.RedisUtil;

import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:42:46
 */

@Service
public class MerchantaccountbankServiceImpl extends YtBaseServiceImpl<Merchantaccountbank, Long>
		implements MerchantaccountbankService {
	@Autowired
	private MerchantaccountbankMapper mapper;

	@Override
	@Transactional
	public Integer post(Merchantaccountbank t) {
		t.setUserid(SysUserContext.getUserId());
		t.setUsername(SysUserContext.getUsername());
		Integer i = mapper.post(t);
		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public YtIPage<Merchantaccountbank> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return YtPageBean.EMPTY_PAGE;
			}
		}
		List<Merchantaccountbank> list = mapper.list(param);
		list.forEach(mab -> {
			mab.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mab.getType()));
		});
		return new YtPageBean<Merchantaccountbank>(param, list, count);
	}

	@Override
	public Merchantaccountbank get(Long id) {
		Merchantaccountbank t = mapper.get(id);
		return t;
	}
}