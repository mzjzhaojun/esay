package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.entity.Menu;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface MenuService extends YtIBaseService<Menu, Long> {
	
	YtIPage<Menu> page(Map<String, Object> param);

	/**
	 * 列表
	 *
	 * @param params 查询参数
	 * @return 菜单信息
	 * @author zhengqingya
	 * @date 2020/9/10 20:30
	 */
	List<SysMenuTreeVO> list(SysMenuTreeDTO params);

	/**
	 * 菜单树
	 *
	 * @param params 查询参数
	 * @return 树
	 * @author zhengqingya
	 * @date 2020/9/10 20:30
	 */
	List<SysMenuTreeVO> tree(SysMenuTreeDTO params);

	/**
	 * 系统所有菜单ids
	 *
	 * @return 菜单ids
	 * @author zhengqingya
	 * @date 2021/1/13 20:44
	 */
	List<Long> allMenuId();

}