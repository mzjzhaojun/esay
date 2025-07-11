package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SysconfigMapper;
import com.yt.app.api.v1.mapper.SysokxMapper;
import com.yt.app.api.v1.service.SysconfigService;

import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

@Service
public class SysconfigServiceImpl extends YtBaseServiceImpl<Sysconfig, Long> implements SysconfigService {

	@Autowired
	private SysconfigMapper mapper;

	@Autowired
	private SysokxMapper sysokxmapper;

	@Override
	@Transactional
	public Integer post(Sysconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Sysconfig> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Sysconfig>(Collections.emptyList());
			}
		}
		List<Sysconfig> list = mapper.list(param);
		return new YtPageBean<Sysconfig>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Sysconfig get(Long id) {
		Sysconfig t = mapper.get(id);
		return t;
	}

	@Override
	public List<Sysokx> getDataTop() {
		List<Sysokx> listpc = sysokxmapper.listTop("");
		return listpc;
	}

	@Override
	public List<Sysokx> getAliPayDataTop() {
		List<Sysokx> listpc = sysokxmapper.listTop("aliPay");
		return listpc;
	}

	@Override
	public Sysokx getDataTopOne() {
		List<Sysokx> listpc = sysokxmapper.listTop("");
		return listpc.get(0);
	}

}