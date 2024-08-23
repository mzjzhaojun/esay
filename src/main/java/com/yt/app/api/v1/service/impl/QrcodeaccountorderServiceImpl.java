package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.service.QrcodeaccountorderService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
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
public class QrcodeaccountorderServiceImpl extends YtBaseServiceImpl<Qrcodeaccountorder, Long>
		implements QrcodeaccountorderService {
	@Autowired
	private QrcodeaccountorderMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public YtIPage<Qrcodeaccountorder> list(Map<String, Object> param) {
		List<Qrcodeaccountorder> list = mapper.list(param);
		return new YtPageBean<Qrcodeaccountorder>(list);
	}

	@Override
	public Qrcodeaccountorder get(Long id) {
		Qrcodeaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<QrcodeaccountorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountorderVO>(Collections.emptyList());
		}
		List<QrcodeaccountorderVO> list = mapper.page(param);
		return new YtPageBean<QrcodeaccountorderVO>(param, list, count);
	}
}