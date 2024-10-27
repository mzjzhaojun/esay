package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeaccountrecordMapper;
import com.yt.app.api.v1.service.QrcodeaccountrecordService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Qrcodeaccountrecord;
import com.yt.app.api.v1.vo.QrcodeaccountrecordVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

@Service
public class QrcodeaccountrecordServiceImpl extends YtBaseServiceImpl<Qrcodeaccountrecord, Long> implements QrcodeaccountrecordService {
	@Autowired
	private QrcodeaccountrecordMapper mapper;

	@Override
	@Transactional
	public Integer post(Qrcodeaccountrecord t) {
		Integer i = mapper.post(t);
		return i;
	}


	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcodeaccountrecord get(Long id) {
		Qrcodeaccountrecord t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeaccountrecordVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeaccountrecordVO>(Collections.emptyList());
		}
		List<QrcodeaccountrecordVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<QrcodeaccountrecordVO>(param, list, count);
	}
}