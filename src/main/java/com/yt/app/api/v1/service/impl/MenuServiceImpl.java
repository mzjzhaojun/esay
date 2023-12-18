package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MenuMapper;
import com.yt.app.api.v1.mapper.RolemenuMapper;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.service.RolemenuService;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.entity.Menu;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

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
public class MenuServiceImpl extends YtBaseServiceImpl<Menu, Long> implements MenuService {

	@Autowired
	private MenuMapper mapper;

	@Autowired
	private RolemenuMapper tsysrolemenumapper;

	@Autowired
	private RolemenuService tsysrolemenuservice;

	@Override
	@Transactional
	public Integer post(Menu t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Menu> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Menu>(Collections.emptyList());
			}
		}
		List<Menu> list = mapper.list(param);
		return new YtPageBean<Menu>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Menu get(Long id) {
		Menu t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		List<SysMenuTreeVO> list = mapper.getByParentId(id);
		Assert.isTrue(CollUtil.isEmpty(list), "请先删除子菜单后再删除当前菜单！");
		tsysrolemenumapper.delByMenuId(id);
		return mapper.delete(id);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysMenuTreeVO> list(SysMenuTreeDTO params) {
		if (params.getIsOnlyShowPerm() == null) {
			params.setIsOnlyShowPerm(false);
		}
		return this.mapper.selectMenuTree(params);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<SysMenuTreeVO> tree(SysMenuTreeDTO params) {
		/*
		 * // 1、查询租户权限 （排除超管） if (!JwtUserContext.hasSuperOrSystemAdmin()) {
		 * SysTenantPackage sysTenantPackage =
		 * this.iSysTenantPackageService.detailReTenantId(TenantIdContext.getTenantId())
		 * ; params.setMenuIdList(sysTenantPackage.getMenuIdList()); }
		 */

		Boolean isOnlySystemAdminRePerm = params.getIsOnlySystemAdminRePerm();
		if ((isOnlySystemAdminRePerm != null && isOnlySystemAdminRePerm) || JwtUserContext.hasSystemAdmin()) {
			// 查询系统管理员有的权限
			params.setMenuIdList(this.tsysrolemenuservice.getMenuIdsByRoleId(AppConstant.SYSTEM_ADMIN_ROLE_ID));
		}

		// 2、拿到所有菜单
		List<SysMenuTreeVO> allMenuList = this.list(params);

		if (StrUtil.isNotBlank(params.getName())) {
			return allMenuList;
		}

		// 3、看看是否需要通过子级菜单去拿到父级数据
		List<Long> childMenuIdList = params.getChildMenuIdList();
		if (CollUtil.isNotEmpty(childMenuIdList)) {
			// 拿到子级第一次的父id
			List<Long> firstParentIdList = allMenuList.stream().filter(e -> childMenuIdList.contains(e.getId()))
					.map(SysMenuTreeVO::getParent_id).collect(Collectors.toList());
			// 拿到子级关联的所有父级菜单
			List<Long> parentIdList = Lists.newArrayList();
			parentIdList.addAll(childMenuIdList);
			List<Long> childReAllParentIdList = recurveAllParentIdList(firstParentIdList, allMenuList, parentIdList);
			if (CollUtil.isNotEmpty(childReAllParentIdList)) {
				allMenuList = allMenuList.stream().filter(e -> childReAllParentIdList.contains(e.getId()))
						.collect(Collectors.toList());
			}
		}

		// 4、遍历出父菜单对应的子菜单 -- 递归
		return this.recurveMenu(AppConstant.PARENT_ID, "", allMenuList);
	}

	/**
	 * 递归菜单 -- 拿到包含子菜单的所有父级菜单
	 *
	 * @param nodeReParentIdList 子级关联的父菜单ids
	 * @param allList            所有菜单
	 * @param parentIdList       最后返回的父id
	 * @return 子菜单与关联的所有父级菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	public static List<Long> recurveAllParentIdList(List<Long> nodeReParentIdList, List<SysMenuTreeVO> allList,
			List<Long> parentIdList) {
		parentIdList.addAll(nodeReParentIdList);
		// 过滤掉顶级父id 0
		List<Long> nodeReParentIdListNew = nodeReParentIdList.stream().filter(e -> !AppConstant.PARENT_ID.equals(e))
				.collect(Collectors.toList());
		if (CollUtil.isEmpty(nodeReParentIdListNew)) {
			// 升序 + 去重
			return parentIdList.stream().distinct().sorted().collect(Collectors.toList());
		}
		List<SysMenuTreeVO> list = allList.stream().filter(item -> nodeReParentIdListNew.contains(item.getId()))
				.collect(Collectors.toList());
		List<Long> childReParentIdList = list.stream().map(SysMenuTreeVO::getParent_id).collect(Collectors.toList());
		return recurveAllParentIdList(childReParentIdList, allList, parentIdList);
	}

	/**
	 * 递归菜单
	 *
	 * @param parentMenuId   父菜单id
	 * @param parentMenuName 父菜单名
	 * @param allMenuList    所有菜单
	 * @return 菜单树列表
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	private List<SysMenuTreeVO> recurveMenu(Long parentMenuId, String parentMenuName, List<SysMenuTreeVO> allMenuList) {
		// 存放子菜单的集合
		List<SysMenuTreeVO> childMenuList = allMenuList.stream().filter(e -> e.getParent_id().equals(parentMenuId))
				.collect(Collectors.toList());
		// 递归
		childMenuList.forEach(item -> {
			Long menuId = item.getId();
			String name = item.getName();
			item.setFullName(parentMenuName + "/" + name);
			// 子菜单
			item.setChildren(this.recurveMenu(menuId, item.getFullName(), allMenuList));
			item.handleData();
		});
		return childMenuList;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Long> allMenuId() {
		return mapper.selectAllMenuId();
	}

}