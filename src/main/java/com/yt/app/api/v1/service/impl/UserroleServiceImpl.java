package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.UserroleMapper;
import com.yt.app.api.v1.service.UserroleService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yt.app.api.v1.bo.SysUserReRoleIdListBO;
import com.yt.app.api.v1.dbo.SysUserRoleSaveDTO;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.collection.CollUtil;

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
public class UserroleServiceImpl extends YtBaseServiceImpl<Userrole, Long> implements UserroleService {
	@Autowired
	private UserroleMapper sysUserRoleMapper;

	@Override
	@Transactional
	public Integer post(Userrole t) {
		Integer i = sysUserRoleMapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Userrole> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = sysUserRoleMapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Userrole>(Collections.emptyList());
			}
		}
		List<Userrole> list = sysUserRoleMapper.list(param);
		return new YtPageBean<Userrole>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Userrole get(Long id) {
		Userrole t = sysUserRoleMapper.get(id);
		return t;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addOrUpdateData(SysUserRoleSaveDTO params) {
		Long userId = params.getUserId();
		List<Long> roleIdList = params.getRoleIdList();

		if (CollUtil.isEmpty(roleIdList)) {
			// 直接删除用户关联的所有角色
			this.delByUserId(userId);
			return;
		}

		// 1、查询用户关联的旧角色信息
		List<Userrole> reRoleListOld = this.sysUserRoleMapper.selectList(new LambdaQueryWrapper<Userrole>().eq(Userrole::getUser_id, userId));
		// 角色id -> 主键id
		Map<Long, Long> roleReIdMapOld = reRoleListOld.stream().collect(Collectors.toMap(Userrole::getRole_id, Userrole::getId, (oldData, newData) -> newData));
		// 旧角色id
		List<Long> reRoleIdListOld = reRoleListOld.stream().map(Userrole::getRole_id).collect(Collectors.toList());

		// 2、筛选出需删除的旧数据
		List<Long> removeRoleIdList = reRoleIdListOld.stream().filter(oldRoleId -> !roleIdList.contains(oldRoleId)).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(removeRoleIdList)) {
			// 删除角色关联的菜单权限信息
			this.sysUserRoleMapper.delete(new LambdaQueryWrapper<Userrole>().eq(Userrole::getUser_id, userId).in(Userrole::getRole_id, removeRoleIdList));
		}

		// 3、再新增角色
		List<Userrole> saveList = Lists.newArrayList();

		roleIdList.forEach(roleId -> saveList.add(Userrole.builder().id(roleReIdMapOld.get(roleId)).user_id(userId).role_id(roleId).build()));

		this.sysUserRoleMapper.insertBatchSomeColumn(saveList);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> listUserId(Long roleId) {
		return this.sysUserRoleMapper.listUserId(roleId);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> listRoleId(Long userId) {
		return this.mapRoleId(Lists.newArrayList(userId)).get(userId);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Map<Long, List<Long>> mapRoleId(List<Long> userIdList) {
		Map<Long, List<Long>> resultMap = Maps.newHashMap();
		if (CollectionUtils.isEmpty(userIdList)) {
			return resultMap;
		}
		List<SysUserReRoleIdListBO> userReRoleIdList = this.sysUserRoleMapper.selectListByUserIds(userIdList);
		for (SysUserReRoleIdListBO item : userReRoleIdList) {
			resultMap.computeIfAbsent(item.getUser_id(), k -> new ArrayList<Long>()).add(item.getRole_id());
		}
		return resultMap;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delByUserId(Long userId) {
		this.sysUserRoleMapper.delete(new LambdaUpdateWrapper<Userrole>().eq(Userrole::getUser_id, userId));
	}
}