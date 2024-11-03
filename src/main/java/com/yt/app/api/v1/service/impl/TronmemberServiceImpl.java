package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronmemberMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.TronmemberService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.TronmemberVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.PasswordUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-08 01:31:33
 */

@Service
public class TronmemberServiceImpl extends YtBaseServiceImpl<Tronmember, Long> implements TronmemberService {
	@Autowired
	private TronmemberMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private MerchantService merchantservice;

	@Override
	@Transactional
	public Integer post(Tronmember t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tronmember get(Long id) {
		Tronmember t = mapper.getByTgId(id);
		if (t.getUserid() == null) {
			Merchant m = new Merchant();
			m.setUsername(t.getName());
			m.setName(t.getName());
			m.setNikname(t.getName());
			m.setCode(t.getTgid().toString());
			m.setStatus(true);
			m.setPassword("123456");
			m.setExchange(0.00);
			m.setCollection(0.00);
			m.setOnecost(0.00);
			m.setDownpoint(0.00);
			m.setExchangeonecost(0.00);
			m.setExchangedownpoint(0.00);
			Merchant mm = merchantservice.postMerchant(m);
			t.setUserid(mm.getUserid());
			t.setTenant_id(m.getTenant_id());
			mapper.put(t);
		}
		User u = usermapper.get(t.getUserid());
		t.setUserid(u.getId());
		t.setPassword(PasswordUtil.decrypt(u.getPassword()));
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TronmemberVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TronmemberVO>(Collections.emptyList());
		}
		List<TronmemberVO> list = mapper.page(param);
		return new YtPageBean<TronmemberVO>(param, list, count);
	}

	@Override

	public Tronmember getByTgId(Long id) {
		return mapper.getByTgId(id);
	}
}