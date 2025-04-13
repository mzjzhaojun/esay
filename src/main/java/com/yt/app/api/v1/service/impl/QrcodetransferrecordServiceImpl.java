package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodetransferrecordMapper;
import com.yt.app.api.v1.service.QrcodetransferrecordService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodetransferrecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-13 22:38:13
 */

@Service
public class QrcodetransferrecordServiceImpl extends YtBaseServiceImpl<Qrcodetransferrecord, Long> implements QrcodetransferrecordService {
	@Autowired
	private QrcodetransferrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodetransferrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Qrcodetransferrecord get(Long id) {
		Qrcodetransferrecord t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodetransferrecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodetransferrecordVO>(Collections.emptyList());
		}
		List<QrcodetransferrecordVO> list = mapper.page(param);
		return new YtPageBean<QrcodetransferrecordVO>(param, list, count);
	}
}