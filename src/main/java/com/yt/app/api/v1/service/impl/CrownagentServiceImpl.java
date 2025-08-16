package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.CrownagentMapper;
import com.yt.app.api.v1.mapper.TgfootballgroupMapper;
import com.yt.app.api.v1.service.CrownagentService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.vo.CrownagentVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.FootBallUtil;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */

@Service
public class CrownagentServiceImpl extends YtBaseServiceImpl<Crownagent, Long> implements CrownagentService {
	@Autowired
	private CrownagentMapper mapper;

	@Autowired
	private TgfootballgroupMapper tgfootballgroupmapper;

	@Override
	@Transactional
	public Integer post(Crownagent t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Crownagent get(Long id) {
		Crownagent t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<CrownagentVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<CrownagentVO>(Collections.emptyList());
		}
		List<CrownagentVO> list = mapper.page(param);
		return new YtPageBean<CrownagentVO>(param, list, count);
	}

	@Override
	@Transactional
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer put(Crownagent t) {
		if (t.getChannelid() != null)
			t.setChannelname(tgfootballgroupmapper.getByTgGroupId(t.getChannelid()).getTggroupname());
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	public Integer login(Crownagent ct) {
		Crownagent t = mapper.get(ct.getId());
		ResponseEntity<String> str = FootBallUtil.LoginFootBall(t.getDomian(), t.getUsername(), t.getPassword(), t.getVer(), t.getOrigin());
		if (str.getStatusCodeValue() == 200) {
			t.setCookie(str.getHeaders().getFirst("Set-Cookie"));
			JSONObject data = JSONUtil.parseObj(str.getBody());
			if (data.getInt("code") == 102) {
				t.setUid(data.getStr("uid"));
			}
			t.setSelmaxid("10");
			t.setStatus(true);
		} else {
			t.setStatus(false);
		}
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}
}