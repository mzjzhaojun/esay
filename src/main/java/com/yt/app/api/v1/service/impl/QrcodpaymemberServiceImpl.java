package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodpaymemberMapper;
import com.yt.app.api.v1.service.QrcodpaymemberService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodpaymember;
import com.yt.app.api.v1.vo.QrcodpaymemberVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-16 23:44:12
 */

@Service
public class QrcodpaymemberServiceImpl extends YtBaseServiceImpl<Qrcodpaymember, Long> implements QrcodpaymemberService {
	@Autowired
	private QrcodpaymemberMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodpaymember t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Qrcodpaymember get(Long id) {
		Qrcodpaymember t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodpaymemberVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodpaymemberVO>(Collections.emptyList());
		}
		List<QrcodpaymemberVO> list = mapper.page(param);
		return new YtPageBean<QrcodpaymemberVO>(param, list, count);
	}
}