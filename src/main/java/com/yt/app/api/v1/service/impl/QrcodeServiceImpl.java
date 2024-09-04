package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.vo.QrcodeVO;
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
 * @version v1 @createdate2024-08-21 14:30:58
 */

@Service
public class QrcodeServiceImpl extends YtBaseServiceImpl<Qrcode, Long> implements QrcodeService {
	@Autowired
	private QrcodeMapper mapper;

	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;

	@Override
	@Transactional
	public Integer post(Qrcode t) {
		t.setUserid(SysUserContext.getUserId());
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Qrcode> list(Map<String, Object> param) {
		List<Qrcode> list = mapper.list(param);
		return new YtPageBean<Qrcode>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcode get(Long id) {
		Qrcode t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		// 删除关联表
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		qrcodeaisleqrcodemapper.deleteByQrcodelId(id);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeVO>(Collections.emptyList());
		}
		List<QrcodeVO> list = mapper.page(param);
		list.forEach(p -> {
			p.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + p.getType()));
		});
		return new YtPageBean<QrcodeVO>(param, list, count);
	}
}