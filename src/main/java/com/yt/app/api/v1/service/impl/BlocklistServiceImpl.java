package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.BlocklistMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.service.BlocklistService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Blocklist;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.BlocklistVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 14:56:50
 */

@Service
public class BlocklistServiceImpl extends YtBaseServiceImpl<Blocklist, Long> implements BlocklistService {
	@Autowired
	private BlocklistMapper mapper;

	@Autowired
	private IncomeMapper incomemapper;

	@Override
	@Transactional
	public Integer post(Blocklist t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	public Blocklist get(Long id) {
		Blocklist t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<BlocklistVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<BlocklistVO>(Collections.emptyList());
		}
		List<BlocklistVO> list = mapper.page(param);
		return new YtPageBean<BlocklistVO>(param, list, count);
	}

	@Override
	public Blocklist getByHexaddress(String hexaddress, String ordernum) {
		Blocklist t = mapper.getByHexaddress(hexaddress);
		if (t == null) {
			Income in = incomemapper.getByOrderNum(ordernum);
			in.setBlockaddress(hexaddress);
			incomemapper.updateBlock(in);
		}
		return t;
	}
}