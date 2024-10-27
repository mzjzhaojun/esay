package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.ProvincecityareaMapper;
import com.yt.app.api.v1.service.ProvincecityareaService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.dbo.SysProvinceCityAreaTreeDTO;
import com.yt.app.api.v1.entity.Provincecityarea;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-03 19:50:02
 */

@Service
public class ProvincecityareaServiceImpl extends YtBaseServiceImpl<Provincecityarea, Long> implements ProvincecityareaService {
	@Autowired
	private ProvincecityareaMapper mapper;

	@Override
	@Transactional
	public Integer post(Provincecityarea t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Provincecityarea> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Provincecityarea>(Collections.emptyList());
			}
		}
		List<Provincecityarea> list = mapper.list(param);
		return new YtPageBean<Provincecityarea>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Provincecityarea get(Long id) {
		Provincecityarea t = mapper.get(id);
		return t;
	}

	@Override

	public List<Provincecityarea> tree(SysProvinceCityAreaTreeDTO params) {
		List<Provincecityarea> list = this.mapper.selectDataList(params);
		List<Provincecityarea> result = list.stream().filter(e -> e.getType().equals(params.getType())).collect(Collectors.toList());
		result.forEach(item -> item.setChildren(this.recurveTree(item.getId(), list)));
		return result;
	}

	/**
	 * 递归树
	 *
	 * @param parentId 父ID
	 * @param allList  所有省市区数据
	 * @return 树列表
	 * @author zhengqingya
	 */
	private List<Provincecityarea> recurveTree(Long parentId, List<Provincecityarea> allList) {
		List<Provincecityarea> list = allList.stream().filter(e -> parentId.equals(e.getParent_id())).collect(Collectors.toList());
		for (Provincecityarea item : list) {
			item.setChildren(this.recurveTree(item.getId(), allList));
		}
		return list;
	}
}