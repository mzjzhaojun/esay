package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.RoleMapper;
import com.yt.app.api.v1.mapper.RolemenuMapper;
import com.yt.app.api.v1.mapper.RolescopeMapper;
import com.yt.app.api.v1.mapper.UserroleMapper;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.service.RoleService;
import com.yt.app.api.v1.service.RolescopeService;
import com.yt.app.api.v1.service.UserService;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.api.v1.vo.SysRoleAllPermissionDetailVO;
import com.yt.app.api.v1.vo.SysRoleBaseVO;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.dbo.SysRoleBaseDTO;
import com.yt.app.api.v1.dbo.SysRoleRePermSaveDTO;
import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.Role;
import com.yt.app.api.v1.entity.Rolemenu;
import com.yt.app.api.v1.entity.Rolescope;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.SysRoleCodeEnum;
import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

@Service
public class RoleServiceImpl extends YtBaseServiceImpl<Role, Long> implements RoleService {
	@Autowired
	private RoleMapper sysRoleMapper;
	@Autowired
	private RolemenuMapper sysrolemenumapper;
	@Autowired
	private RolescopeMapper sysrolescopemapper;
	@Autowired
	private MenuService tsysmenuservice;
	@Autowired
	private Snowflake idworker;
	@Autowired
	private RolescopeService tsysrolescopeservice;
	@Autowired
	private MenuService isysmenuservice;
	@Autowired
	private UserService isysuserservice;
	@Autowired
	private UserroleMapper userrolemapper;

	@Override
	@Transactional
	public Integer post(Role t) {
		Integer i = sysRoleMapper.post(t);
		return i;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		Role sysRole = sysRoleMapper.get(id);
		if (sysRole.getIs_fixed()) {
			Assert.isTrue(JwtUserContext.hasSuperAdmin(), "您没有权限删除固定角色！");
		}
		if (sysRole.getIs_refresh_all_tenant()) {
			Assert.isTrue(JwtUserContext.hasSuperOrSystemAdmin(), "您没有权限同步更新所有租户下的角色数据！");
		}

		// 1、删除该角色下关联的权限
		this.sysrolemenumapper.delByRoleId(id);
		this.sysrolescopemapper.delByRoleId(id);

		return sysRoleMapper.delete(id);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Role> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = sysRoleMapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Role>(Collections.emptyList());
			}
		}
		List<Role> list = sysRoleMapper.list(param);
		return new YtPageBean<Role>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Role get(Long id) {
		Role t = sysRoleMapper.get(id);
		Assert.notNull(t, "角色不存在！");
		// 2、菜单权限树
		List<SysMenuTreeVO> menuTree = tsysmenuservice.tree(SysMenuTreeDTO.builder().roleIdList(Lists.newArrayList(id)).build());
		t.setMenuTree(menuTree);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public IPage<SysRoleBaseVO> listPage(SysRoleBaseDTO params) {
		return this.sysRoleMapper.selectDataList(new Page<>(), params);
	}

	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> selectListByRoleId(Long roleid) {
		return tsysrolescopeservice.getScopeIdListByRoleId(roleid);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysRoleBaseVO> list(SysRoleBaseDTO params) {
		return this.sysRoleMapper.selectDataList(params);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Role detail(Long roleId) {
		Role sysRole = this.sysRoleMapper.selectById(roleId);
		Assert.notNull(sysRole, "角色不存在！");
		return sysRole;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Map<Long, String> mapByRoleIdList(List<Long> roleIdList) {
		List<Role> list = this.sysRoleMapper.selectList(new LambdaQueryWrapper<Role>().in(Role::getId, roleIdList));
		return list.stream().collect(Collectors.toMap(Role::getId, Role::getName, (oldData, newData) -> newData));
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysRoleBaseVO> tree(SysRoleBaseDTO params) {
		List<SysRoleBaseVO> list = this.list(params);
		if (CollUtil.isEmpty(list)) {
			return Lists.newArrayList();
		}
		if (StrUtil.isNotBlank(params.getName())) {
			return list;
		}
		return this.recurveRole(AppConstant.PARENT_ID, list, params.getExcludeRoleId());
	}

	/**
	 * 递归
	 *
	 * @param parentId      父id
	 * @param allList       所有数据
	 * @param excludeRoleId 排除指定角色id下级的数据
	 * @return 菜单树列表
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	private List<SysRoleBaseVO> recurveRole(Long parentId, List<SysRoleBaseVO> allList, Long excludeRoleId) {
		if (parentId.equals(excludeRoleId)) {
			return Lists.newArrayList();
		}
		// 存放子集合
		List<SysRoleBaseVO> childList = allList.stream().filter(e -> e.getParent_id().equals(parentId)).collect(Collectors.toList());
		// 递归
		childList.forEach(item -> {
			item.setChildren(this.recurveRole(item.getId(), allList, excludeRoleId));
			item.handleData();
		});
		return childList;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> getChildRoleIdList(Long roleId) {
		return this.recurveRoleId(roleId, this.list(), Lists.newArrayList());
	}

	/**
	 * 递归
	 *
	 * @param parentId   父id
	 * @param allList    所有数据
	 * @param roleIdList 最终结果
	 * @return 菜单树列表
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	private List<Long> recurveRoleId(Long parentId, List<Role> allList, List<Long> roleIdList) {
		roleIdList.add(parentId);
		List<Role> childList = allList.stream().filter(e -> e.getParent_id().equals(parentId)).collect(Collectors.toList());
		if (CollUtil.isEmpty(childList)) {
			return roleIdList;
		}
		for (Role item : childList) {
			this.recurveRoleId(item.getId(), allList, roleIdList);
		}
		return roleIdList;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Long getRoleIdByCode(SysRoleCodeEnum sysRoleCodeEnum) {
		return this.sysRoleMapper.selectRoleIdByCode(sysRoleCodeEnum.getCode());
	}

	@Override
	@Transactional
	public Integer saverolereperm(SysRoleRePermSaveDTO param) {
		Long roleId = param.getRoleId();
		sysrolemenumapper.delByRoleId(roleId);
		sysrolescopemapper.delByRoleId(roleId);
		List<Rolemenu> rmlist = new ArrayList<Rolemenu>();
		param.getMenuIdList().forEach(e -> {
			Rolemenu tsrm = new Rolemenu();
			tsrm.setId(idworker.nextId());
			tsrm.setTenant_id(TenantIdContext.getTenantId());
			tsrm.setRole_id(roleId);
			tsrm.setMenu_id(e);
			rmlist.add(tsrm);
		});
		if (rmlist.size() > 0)
			sysrolemenumapper.batchSava(rmlist);
		List<Rolescope> rslist = new ArrayList<Rolescope>();
		param.getScopeIdList().forEach(e -> {
			Rolescope tsrs = new Rolescope();
			tsrs.setId(idworker.nextId());
			tsrs.setTenant_id(TenantIdContext.getTenantId());
			tsrs.setRole_id(roleId);
			tsrs.setScope_id(e);
			rslist.add(tsrs);
		});
		if (rslist.size() > 0)
			sysrolescopemapper.batchSava(rslist);
		return 1;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public SysUserPermVO getUserPerm(SysUserPermDTO params) {
		// 1、拿到用户基础信息
		SysUserPermVO userPerm = this.isysuserservice.getUserPerm(params);

		// 2、权限树
		userPerm.setPermissionTreeList(this.isysmenuservice.tree(SysMenuTreeDTO.builder().roleIdList(userPerm.getRoleIdList()).isOnlyShowPerm(true).build()));

		userPerm.handleData();
		return userPerm;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public SysRoleAllPermissionDetailVO permissionDetail(Long roleId) {
		// 1、角色基本信息
		Role sysRole = this.get(roleId);
		Assert.notNull(sysRole, "角色不存在！");

		// 2、菜单权限树
		List<SysMenuTreeVO> menuTree = this.isysmenuservice.tree(SysMenuTreeDTO.builder().roleIdList(Lists.newArrayList(roleId)).build());

		return SysRoleAllPermissionDetailVO.builder().roleId(sysRole.getId()).name(sysRole.getName()).code(sysRole.getCode()).status(sysRole.getStatus()).menuTree(menuTree).build();
	}

	@Transactional
	public void refreshSuperAdminPerm() {
		// 1、先查询超级管理员角色id再绑定
		Long roleId = this.sysRoleMapper.selectRoleIdByCode(SysRoleCodeEnum.超级管理员.getCode());
		Assert.notNull(roleId, "超级管理员角色丢失！");
		Userrole ur = Userrole.builder().user_id(AppConstant.SYSTEM_SUPER_ADMIN_USER_ID).role_id(roleId).build();
		List<Userrole> reusult = userrolemapper.getByRoleIdUserId(ur);
		if (reusult.size() == 0) {
			// 2、先查询所有菜单和按钮数据
			List<Long> menuIdList = this.isysmenuservice.allMenuId();
			// 3、保存角色关联的菜单和按钮权限
			sysrolemenumapper.delByRoleId(roleId);
			List<Rolemenu> rmlist = new ArrayList<Rolemenu>();
			menuIdList.forEach(e -> {
				Rolemenu tsrm = new Rolemenu();
				tsrm.setId(idworker.nextId());
				tsrm.setTenant_id(TenantIdContext.getTenantId());
				tsrm.setRole_id(roleId);
				tsrm.setMenu_id(e);
				rmlist.add(tsrm);
			});
			sysrolemenumapper.batchSava(rmlist);
		}

	}

}