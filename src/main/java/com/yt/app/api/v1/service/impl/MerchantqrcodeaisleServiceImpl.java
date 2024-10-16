package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantqrcodeaisleMapper;
import com.yt.app.api.v1.service.MerchantqrcodeaisleService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.vo.MerchantqrcodeaisleVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 16:58:38
 */

@Service
public class MerchantqrcodeaisleServiceImpl extends YtBaseServiceImpl<Merchantqrcodeaisle, Long> implements MerchantqrcodeaisleService {
	@Autowired
	private MerchantqrcodeaisleMapper mapper;

	@Override
	@Transactional
	public Integer post(Merchantqrcodeaisle t) {
		Merchantqrcodeaisle m = mapper.getByMidAid(t.getQrcodeaisleid(), t.getMerchantid());
		Assert.isNull(m, "已经存在的通道");
		t.setCollection(8.8);
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Merchantqrcodeaisle> list(Map<String, Object> param) {
		List<Merchantqrcodeaisle> list = mapper.list(param);
		return new YtPageBean<Merchantqrcodeaisle>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchantqrcodeaisle get(Long id) {
		Merchantqrcodeaisle t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<MerchantqrcodeaisleVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<MerchantqrcodeaisleVO>(Collections.emptyList());
		}
		List<MerchantqrcodeaisleVO> list = mapper.page(param);
		list.forEach(p -> {
			p.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + p.getType()));
		});
		return new YtPageBean<MerchantqrcodeaisleVO>(param, list, count);
	}
}