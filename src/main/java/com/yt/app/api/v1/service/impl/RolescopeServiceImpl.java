package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.RolescopeMapper;
import com.yt.app.api.v1.service.RolescopeService;
import com.yt.app.api.v1.vo.SysRoleScopeListVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.bo.SysScopeDataBO;
import com.yt.app.api.v1.dbo.SysRoleReScopeSaveDTO;
import com.yt.app.api.v1.entity.Rolescope;
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
public class RolescopeServiceImpl extends YtBaseServiceImpl<Rolescope, Long> implements RolescopeService {
	@Autowired
	private RolescopeMapper sysRoleScopeMapper;

	@Override
	@Transactional
	public Integer post(Rolescope t) {
		Integer i = sysRoleScopeMapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Rolescope> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = sysRoleScopeMapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Rolescope>(Collections.emptyList());
			}
		}
		List<Rolescope> list = sysRoleScopeMapper.list(param);
		return new YtPageBean<Rolescope>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Rolescope get(Long id) {
		Rolescope t = sysRoleScopeMapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> getScopeIdListByRoleId(Long roleId) {
		List<SysRoleScopeListVO> list = sysRoleScopeMapper.selectListByRoleId(roleId);
		return list.stream().map(SysRoleScopeListVO::getScopeId).collect(Collectors.toList());
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysScopeDataBO> getScopeListReRoleIdList(List<Long> roleIdList) {
		return this.sysRoleScopeMapper.selectScopeListReRoleIdList(roleIdList);
	}

	@Override
	@Transactional
	public void saveScopeData(SysRoleReScopeSaveDTO params) {
		Long roleId = params.getRoleId();
		List<Long> scopeIdList = params.getScopeIdList();

		if (CollUtil.isEmpty(scopeIdList)) {
			// 直接删除角色关联的所有数据权限
			this.delByRoleId(roleId);
			return;
		}

		// 1、查询角色关联的旧菜单权限信息
		List<Rolescope> roleReDataListOld = this.sysRoleScopeMapper.selectList(new LambdaQueryWrapper<Rolescope>().eq(Rolescope::getRole_id, roleId));
		// 权限id -> 主键id
		Map<Long, Long> mapOld = roleReDataListOld.stream().collect(Collectors.toMap(Rolescope::getScope_id, Rolescope::getId, (oldData, newData) -> newData));
		// 旧权限id
		List<Long> roleReScopeIdListOld = roleReDataListOld.stream().map(Rolescope::getScope_id).collect(Collectors.toList());

		// 2、拿到要删除的旧权限id
		List<Long> delScopeIdList = CollUtil.subtractToList(roleReScopeIdListOld, scopeIdList);
		if (CollUtil.isNotEmpty(delScopeIdList)) {
			// 删除角色关联的权限信息
			this.delByScopeIdList(delScopeIdList);
		}

		// 3、再保存角色关联的权限信息
		List<Rolescope> roleScopeList = Lists.newArrayList();
		scopeIdList.forEach(scopeId -> roleScopeList.add(Rolescope.builder().id(mapOld.get(scopeId)).role_id(roleId).scope_id(scopeId).build()));
		this.sysRoleScopeMapper.insertBatchSomeColumn(roleScopeList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delByRoleId(Long roleId) {
		this.sysRoleScopeMapper.delete(new LambdaQueryWrapper<Rolescope>().eq(Rolescope::getRole_id, roleId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delReMenuIdList(List<Long> delMenuIdList) {
		if (CollUtil.isEmpty(delMenuIdList)) {
			return;
		}
		this.sysRoleScopeMapper.delReMenuIdList(delMenuIdList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delByScopeIdList(List<Long> scopeIdList) {
		this.sysRoleScopeMapper.delete(new LambdaQueryWrapper<Rolescope>().in(Rolescope::getScope_id, scopeIdList));
	}

}