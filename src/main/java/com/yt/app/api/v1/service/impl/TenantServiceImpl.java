package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.RoleMapper;
import com.yt.app.api.v1.mapper.RolemenuMapper;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.mapper.TenantMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.mapper.UserroleMapper;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.service.TenantService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.BaseConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Role;
import com.yt.app.api.v1.entity.Rolemenu;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.entity.Tenant;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.SysRoleCodeEnum;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PasswordUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Snowflake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-23 20:33:10
 */

@Service
public class TenantServiceImpl extends YtBaseServiceImpl<Tenant, Long> implements TenantService {
	@Autowired
	private TenantMapper mapper;

	@Autowired
	private UserMapper usermapper;

	@Autowired
	private RoleMapper rolemapper;

	@Autowired
	private UserroleMapper userrolemapper;

	@Autowired
	private RolemenuMapper rolemenumapper;

	@Autowired
	private MenuService menuservice;

	@Autowired
	private Snowflake idworker;

	@Autowired
	private SystemaccountMapper systemaccountmapper;

	@Override
	@Transactional
	public Integer post(Tenant params) {

		// Tenant
		Integer i = mapper.post(params);

		// user
		User u = new User();
		u.setTenant_id(params.getId());
		u.setUsername(params.getUsername());
		u.setNickname(params.getAdmin_name());
		u.setPhone(params.getAdmin_phone());
		u.setPassword(PasswordUtil.encodePassword(params.getPassword()));
		u.setAccounttype(DictionaryResource.SYSTEM_ADMINTYPE_1);
		u.setTwofactorcode(StringUtil.getUUID());
		i = usermapper.postAndTanantId(u);

		//
		Systemaccount sm = new Systemaccount();
		sm.setTenant_id(params.getId());
		sm.setTotalincome(0.00);
		sm.setWithdrawamount(0.00);
		sm.setUserid(u.getId());
		systemaccountmapper.postAndTanantId(sm);

		// role
		Role r = new Role();
		r.setTenant_id(u.getTenant_id());
		r.setParent_id(BaseConstant.PARENT_ID);
		r.setName(SysRoleCodeEnum.超级管理员.getName());
		r.setCode(SysRoleCodeEnum.超级管理员.getCode());
		r.setIs_fixed(true);
		r.setStatus(1);
		r.setIs_refresh_all_tenant(true);
		rolemapper.postAndTanantId(r);

		// Userrole
		Userrole ur = new Userrole();
		ur.setUser_id(u.getId());
		ur.setTenant_id(u.getTenant_id());
		ur.setRole_id(r.getId());
		userrolemapper.postAndTanantId(ur);

		// rolemenu
		List<Long> menuIdList = this.menuservice.allMenuId();
		List<Rolemenu> rmlist = new ArrayList<Rolemenu>();
		menuIdList.forEach(e -> {
			Rolemenu tsrm = new Rolemenu();
			tsrm.setId(idworker.nextId());
			tsrm.setTenant_id(u.getTenant_id());
			tsrm.setRole_id(r.getId());
			tsrm.setMenu_id(e);
			rmlist.add(tsrm);
		});
		rolemenumapper.batchSava(rmlist);

		params.setAdmin_user_id(u.getId());
		mapper.put(params);

		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tenant> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Tenant>(Collections.emptyList());
			}
		}
		List<Tenant> list = mapper.list(param);
		return new YtPageBean<Tenant>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tenant get(Long id) {
		Tenant t = mapper.get(id);
		return t;
	}
}