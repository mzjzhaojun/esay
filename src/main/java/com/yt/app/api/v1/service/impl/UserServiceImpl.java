package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.DeptMapper;
import com.yt.app.api.v1.mapper.RoleMapper;
import com.yt.app.api.v1.mapper.TenantMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.mapper.UserroleMapper;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.service.UserService;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.bo.SysUserReRoleIdListBO;
import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.Dept;
import com.yt.app.api.v1.entity.Role;
import com.yt.app.api.v1.entity.Tenant;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.AuthUtil;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.PasswordUtil;

import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */

@Service
public class UserServiceImpl extends YtBaseServiceImpl<User, Long> implements UserService {
	@Autowired
	private UserMapper mapper;

	@Autowired
	private UserroleMapper userrolemapper;

	@Autowired
	private RoleMapper rolemapper;

	@Autowired
	private DeptMapper deptmapper;

	@Autowired
	private TenantMapper tenantmapper;

	@Autowired
	private MenuService tsysmenuservice;

	@Override
	@Transactional
	public Integer post(User t) {
		t.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		t.setAccounttype(JwtUserContext.get().getAccounttype());
		t.setTwofactorcode(GoogleAuthenticatorUtil.getSecretKey());
		Integer i = mapper.post(t);
		if (t.getRoleIdList().size() > 0) {
			Userrole tsur = new Userrole();
			t.getRoleIdList().forEach(r -> {
				tsur.setId(null);
				tsur.setRole_id(r);
				tsur.setUser_id(t.getId());
				userrolemapper.post(tsur);
			});
		}
		Assert.equals(i, 1, ServiceConstant.ADD_FAIL_MSG);
		return i;
	}

	@Override
	@Transactional
	public Integer put(User t) {
		if (t.getPassword() != null && t.getPassword().length() < 30) {
			t.setPassword(PasswordUtil.encodePassword(t.getPassword()));
			AuthUtil.logout(t.getToken());
		}
		Integer i = mapper.put(t);
		if (t.getRoleIdList() != null && t.getRoleIdList().size() > 0) {
			userrolemapper.deleteabyUid(t.getId());
			Userrole tsur = new Userrole();
			t.getRoleIdList().forEach(r -> {
				tsur.setId(null);
				tsur.setRole_id(r);
				tsur.setUser_id(t.getId());
				userrolemapper.post(tsur);
			});
		}
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<User> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<User>(Collections.emptyList());
			}
		}
		List<User> list = mapper.list(param);
		List<Long> userIdList = list.stream().map(User::getId).collect(Collectors.toList());
		long[] deptidList = list.stream().filter(d -> d.getDept_id() != null).mapToLong(User::getDept_id).toArray();
		List<SysUserReRoleIdListBO> listuserrole = userrolemapper.selectListByUserIds(userIdList);
		long[] roleidList = listuserrole.stream().mapToLong(SysUserReRoleIdListBO::getRole_id).toArray();
		List<Role> listsysroel = rolemapper.listByArrayId(roleidList);
		List<Dept> listdept = deptmapper.listByArrayId(deptidList);

		list.forEach(u -> {
			List<Long> roleIdList = new ArrayList<Long>();
			listuserrole.forEach(ur -> {
				if (u.getId().longValue() == ur.getUser_id().longValue()) {
					roleIdList.add(ur.getRole_id());
				}
			});
			u.setRoleIdList(roleIdList);
			StringJoiner sj = new StringJoiner(",");
			roleIdList.forEach(r -> {
				listsysroel.forEach(r2 -> {
					if (r.longValue() == r2.getId().longValue()) {
						sj.add(r2.getName());
					}
				});
			});
			listdept.forEach(d -> {
				if (u.getDept_id() != null && u.getDept_id().longValue() == d.getId().longValue()) {
					u.setDeptName(d.getName());
				}
			});
			u.handleData();
			u.setRoleNames(sj.toString());
		});
		return new YtPageBean<User>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public User get(Long id) {
		User t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public SysUserPermVO getUserPerm(SysUserPermDTO params) {
		// 1.
		SysUserPermVO userPerm = mapper.selectUserPerm(params);

		Assert.notNull(userPerm, "用户不存在或无权限！");

		// 2.权限树
		userPerm.setPermissionTreeList(tsysmenuservice
				.tree(SysMenuTreeDTO.builder().roleIdList(userPerm.getRoleIdList()).isOnlyShowPerm(true).build()));
		// 3.租户id
		Tenant t = tenantmapper.get(userPerm.getTenantId());
		userPerm.setTenantId(t.getId());
		userPerm.setTenantName(t.getName());
		return userPerm;
	}

	@Override
	@Transactional
	public Integer resetpassword(User t) {
		if (t.getPassword() != null && t.getPassword().length() < 30) {
			t.setPassword(PasswordUtil.encodePassword(t.getPassword()));
		}
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.UPDATE_FAIL_MSG);
		AuthUtil.logout(t.getToken());
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Integer checkgoogle(Long code) {
		User u = mapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), code, System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		return 1;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		User t = mapper.get(id);
		t.setIs_deleted(DictionaryResource.IS_DELETE_1);
		Integer i = mapper.put(t);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		return i;
	}
}