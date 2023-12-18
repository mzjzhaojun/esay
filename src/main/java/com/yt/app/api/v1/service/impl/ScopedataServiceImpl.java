package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ScopedataMapper;
import com.yt.app.api.v1.service.MenuService;
import com.yt.app.api.v1.service.ScopedataService;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.dbo.SysScopeDataBaseDTO;
import com.yt.app.api.v1.entity.Scopedata;
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
public class ScopedataServiceImpl extends YtBaseServiceImpl<Scopedata, Long> implements ScopedataService {
	@Autowired
	private ScopedataMapper mapper;
	@Autowired
	private MenuService tsysmenuservice;

	@Override
	@Transactional
	public Integer post(Scopedata t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Scopedata> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Scopedata>(Collections.emptyList());
			}
		}
		List<Scopedata> list = mapper.list(param);
		return new YtPageBean<Scopedata>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Scopedata get(Long id) {
		Scopedata t = mapper.get(id);
		return t;
	}

	public List<Scopedata> list(SysScopeDataBaseDTO params) {
		return this.mapper.selectDataList(params);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Scopedata> tree(SysScopeDataBaseDTO params) {
		// 数据权限
		List<Scopedata> scopeList = this.list(params);
		List<Long> menuIdList = scopeList.stream().map(Scopedata::getMenu_id).distinct().sorted()
				.collect(Collectors.toList());
		Map<Long, List<Scopedata>> scopeMap = scopeList.stream()
				.collect(Collectors.groupingBy(Scopedata::getMenu_id, Collectors.mapping(t -> t, Collectors.toList())));

		// 菜单
		List<SysMenuTreeVO> menuTree = this.tsysmenuservice
				.tree(SysMenuTreeDTO.builder().childMenuIdList(menuIdList).type(1).build());

		// 组装树
		return this.recurveData(scopeMap, menuTree);
	}

	/**
	 * 递归
	 *
	 * @param scopeMap 菜单id -> 数据权限
	 * @param list     所有数据
	 * @return 菜单树列表
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	private List<Scopedata> recurveData(Map<Long, List<Scopedata>> scopeMap, List<SysMenuTreeVO> list) {
		List<Scopedata> result = Lists.newArrayList();
		list.forEach(item -> {
			Long menuId = item.getId();
			List<Scopedata> childList = Lists.newArrayList();
			List<SysMenuTreeVO> children = item.getChildren();
			if (CollUtil.isNotEmpty(children)) {
				childList = this.recurveData(scopeMap, children);
			}

			List<Scopedata> scopeList = scopeMap.get(menuId);
			if (CollUtil.isNotEmpty(scopeList)) {
				childList.addAll(scopeList);
			}
			result.add(Scopedata.builder().custom_id("m" + menuId).custom_name(item.getName())
					.menuFullName(item.getFullName()).children(childList).build());
		});
		return result;
	}
}