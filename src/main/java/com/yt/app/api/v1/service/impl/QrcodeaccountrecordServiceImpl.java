package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeaccountrecordMapper;
import com.yt.app.api.v1.service.QrcodeaccountrecordService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaccountrecord;
import com.yt.app.api.v1.vo.QrcodeaccountrecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

@Service
public class QrcodeaccountrecordServiceImpl extends YtBaseServiceImpl<Qrcodeaccountrecord, Long>
		implements QrcodeaccountrecordService {
	@Autowired
	private QrcodeaccountrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaccountrecord t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Qrcodeaccountrecord> list(Map<String, Object> param) {
		List<Qrcodeaccountrecord> list = mapper.list(param);
		return new YtPageBean<Qrcodeaccountrecord>(list);
	}

	@Override
	public Qrcodeaccountrecord get(Long id) {
		Qrcodeaccountrecord t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodeaccountrecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountrecordVO>(Collections.emptyList());
		}
		List<QrcodeaccountrecordVO> list = mapper.page(param);
		return new YtPageBean<QrcodeaccountrecordVO>(param, list, count);
	}
}