package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.MerchantqrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.service.QrcodeaisleService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.vo.QrcodeaisleVO;
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
 * @version v1 @createdate2024-08-22 14:27:18
 */

@Service
public class QrcodeaisleServiceImpl extends YtBaseServiceImpl<Qrcodeaisle, Long> implements QrcodeaisleService {
	@Autowired
	private QrcodeaisleMapper mapper;

	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;

	@Autowired
	private MerchantqrcodeaisleMapper merchantqrcodeaislemapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaisle t) {
		t.setUserid(SysUserContext.getUserId());
		Integer i = mapper.post(t);
		return i;
	}


	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcodeaisle get(Long id) {
		Qrcodeaisle t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		// 删除关联表
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		qrcodeaisleqrcodemapper.deleteByQrcodeaisleId(id);
		merchantqrcodeaislemapper.deleteByQrcodeaisleId(id);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeaisleVO> page(Map<String, Object> param) {
		if (param.get("merchantid") != null) {
			List<Merchantqrcodeaisle> listmqas = merchantqrcodeaislemapper.getByMid(Long.valueOf(param.get("merchantid").toString()));
			if (listmqas.size() > 0) {
				long[] qraids = listmqas.stream().mapToLong(mqa -> mqa.getQrcodeaisleid()).distinct().toArray();
				param.put("existids", qraids);
			}
		}

		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaisleVO>(Collections.emptyList());
		}
		List<QrcodeaisleVO> list = mapper.page(param);
		list.forEach(al -> {
			al.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + al.getType()));
		});
		return new YtPageBean<QrcodeaisleVO>(param, list, count);
	}
}