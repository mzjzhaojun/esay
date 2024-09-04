package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.service.QrcodeaisleqrcodeService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.QrcodeaisleqrcodeVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

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
public class QrcodeaisleqrcodeServiceImpl extends YtBaseServiceImpl<Qrcodeaisleqrcode, Long>
		implements QrcodeaisleqrcodeService {
	@Autowired
	private QrcodeaisleqrcodeMapper mapper;

	@Autowired
	private QrcodeaisleMapper qrcodeaislemapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaisleqrcode t) {
		Qrcodeaisleqrcode m = mapper.getByAidCid(t.getQrcodelid(), t.getQrcodeaisleid());
		Assert.isNull(m, "已经存在的码");
		Qrcodeaisle as = qrcodeaislemapper.get(t.getQrcodeaisleid());
		as.setQrcodecount(as.getQrcodecount() + 1);
		qrcodeaislemapper.put(as);
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Qrcodeaisleqrcode t = mapper.get(id);
		Qrcodeaisle as = qrcodeaislemapper.get(t.getQrcodeaisleid());
		as.setQrcodecount(as.getQrcodecount() - 1);
		qrcodeaislemapper.put(as);
		return mapper.delete(id);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Qrcodeaisleqrcode> list(Map<String, Object> param) {
		List<Qrcodeaisleqrcode> list = mapper.list(param);
		return new YtPageBean<Qrcodeaisleqrcode>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcodeaisleqrcode get(Long id) {
		Qrcodeaisleqrcode t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeaisleqrcodeVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaisleqrcodeVO>(Collections.emptyList());
		}
		List<QrcodeaisleqrcodeVO> list = mapper.page(param);
		return new YtPageBean<QrcodeaisleqrcodeVO>(param, list, count);
	}
}