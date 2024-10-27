package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysDeptTreeDTO;
import com.yt.app.api.v1.entity.Dept;
import com.yt.app.api.v1.vo.SysDeptTreeVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DeptService extends YtIBaseService<Dept, Long> {
	YtIPage<Dept> page(Map<String, Object> param);

	/**
	 * 树
	 *
	 * @param params 查询参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/09 18:10
	 */
	List<SysDeptTreeVO> tree(SysDeptTreeDTO params);
}