package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.DeptMapper;
import com.yt.app.api.v1.service.DeptService;
import com.yt.app.api.v1.vo.SysDeptTreeVO;

import com.yt.app.common.base.constant.AppConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.google.common.collect.Lists;
import com.yt.app.api.v1.dbo.SysDeptTreeDTO;
import com.yt.app.api.v1.entity.Dept;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

@Service
public class DeptServiceImpl extends YtBaseServiceImpl<Dept, Long> implements DeptService {
	@Autowired
	private DeptMapper mapper;

	@Override
	@Transactional
	public Integer post(Dept t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override

	public YtIPage<Dept> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Dept>(Collections.emptyList());
			}
		}
		List<Dept> list = mapper.list(param);
		return new YtPageBean<Dept>(param, list, count);
	}

	@Override

	public Dept get(Long id) {
		Dept t = mapper.get(id);
		return t;
	}

	@Override

	public List<SysDeptTreeVO> tree(SysDeptTreeDTO params) {
		Map<String, Object> param = BeanUtil.beanToMap(params);
		List<SysDeptTreeVO> list = this.mapper.selectDataList(param);
		if (CollUtil.isEmpty(list)) {
			return Lists.newArrayList();
		}
		if (params == null || StrUtil.isNotBlank(params.getName())) {
			return list;
		}
		return this.recurveDept(AppConstant.PARENT_ID, list, params.getExcludeDeptId());
	}

	/**
	 * 递归部门
	 *
	 * @param parentId      父id
	 * @param allList       所有部门
	 * @param excludeDeptId 排除指定部门id下级的数据
	 * @return 菜单树列表
	 * @author zhengqingya
	 * @date 2020/9/10 20:56
	 */
	private List<SysDeptTreeVO> recurveDept(Long parentId, List<SysDeptTreeVO> allList, Long excludeDeptId) {
		if (parentId.equals(excludeDeptId)) {
			return Lists.newArrayList();
		}
		// 存放子集合
		List<SysDeptTreeVO> childList = allList.stream().filter(e -> e.getParent_id().equals(parentId)).collect(Collectors.toList());
		// 递归
		childList.forEach(item -> {
			item.setChildren(this.recurveDept(item.getId(), allList, excludeDeptId));
			item.handleData();
		});
		return childList;
	}
}