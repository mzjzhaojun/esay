package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.RolemenuMapper;
import com.yt.app.api.v1.service.RolemenuService;
import com.yt.app.api.v1.vo.SysRoleReBtnPermListVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.dbo.SysRoleReMenuSaveDTO;
import com.yt.app.api.v1.entity.Rolemenu;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.collection.CollUtil;

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
public class RolemenuServiceImpl extends YtBaseServiceImpl<Rolemenu, Long> implements RolemenuService {
	@Autowired
	private RolemenuMapper sysRoleMenuMapper;

	@Override
	@Transactional
	public Integer post(Rolemenu t) {
		Integer i = sysRoleMenuMapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Rolemenu> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = sysRoleMenuMapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Rolemenu>(Collections.emptyList());
			}
		}
		List<Rolemenu> list = sysRoleMenuMapper.list(param);
		return new YtPageBean<Rolemenu>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Rolemenu get(Long id) {
		Rolemenu t = sysRoleMenuMapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> getMenuIdsByRoleId(Long roleId) {
		return this.sysRoleMenuMapper.selectMenuIdsByRoleId(roleId);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> getMenuIdsByRoleIds(List<Long> roleIdList) {
		return this.sysRoleMenuMapper.selectMenuIdsByRoleIds(roleIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRoleMenuIds(SysRoleReMenuSaveDTO params) {
		Long roleId = params.getRoleId();
		List<Long> menuIdList = params.getMenuIdList();

		if (CollUtil.isEmpty(menuIdList)) {
			// 直接删除角色关联的所有菜单权限
			this.delByRoleId(roleId);
			return;
		}

		// 1、查询角色关联的旧菜单权限信息
		List<Rolemenu> roleReMenuListOld = this.sysRoleMenuMapper.selectList(new LambdaQueryWrapper<Rolemenu>().eq(Rolemenu::getRole_id, roleId));
		// 菜单id -> 主键id
		Map<Long, Long> menuReIdMapOld = roleReMenuListOld.stream().collect(Collectors.toMap(Rolemenu::getMenu_id, Rolemenu::getId, (oldData, newData) -> newData));
		// 旧菜单id
		List<Long> roleReMenuIdListOld = roleReMenuListOld.stream().map(Rolemenu::getMenu_id).collect(Collectors.toList());

		// 2、筛选出需删除的旧数据
		List<Long> removeMenuIdList = roleReMenuIdListOld.stream().filter(oldMenuId -> !menuIdList.contains(oldMenuId)).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(removeMenuIdList)) {
			// 删除角色关联的菜单权限信息
			this.sysRoleMenuMapper.delete(new LambdaQueryWrapper<Rolemenu>().eq(Rolemenu::getRole_id, roleId).in(Rolemenu::getMenu_id, removeMenuIdList));
		}

		// 3、再保存角色关联的菜单权限信息
		List<Rolemenu> roleMenuList = Lists.newArrayList();
		menuIdList.forEach(menuId -> roleMenuList.add(Rolemenu.builder().id(menuReIdMapOld.get(menuId)).role_id(roleId).menu_id(menuId).build()));
		this.sysRoleMenuMapper.insertBatchSomeColumn(roleMenuList);
	}

	@Override
	@Transactional
	public void delByRoleId(Long roleId) {
		this.sysRoleMenuMapper.delete(new LambdaQueryWrapper<Rolemenu>().eq(Rolemenu::getRole_id, roleId));
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Integer> getMenuIdList(Integer tenantId) {
		return this.sysRoleMenuMapper.selectMenuIdList(tenantId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delReMenuIdList(List<Integer> delMenuIdList) {
		if (CollUtil.isEmpty(delMenuIdList)) {
			return;
		}
		this.sysRoleMenuMapper.delete(new LambdaQueryWrapper<Rolemenu>().in(Rolemenu::getMenu_id, delMenuIdList).notIn(Rolemenu::getRole_id, AppConstant.NOT_DEL_MENU_EXCLUDE_ROLE_ID_LIST));
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysRoleReBtnPermListVO> listRoleReBtnPerm() {
		return this.sysRoleMenuMapper.selectBtnPerm();
	}
}