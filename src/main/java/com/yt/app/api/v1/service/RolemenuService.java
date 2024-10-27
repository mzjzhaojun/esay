package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysRoleReMenuSaveDTO;
import com.yt.app.api.v1.entity.Rolemenu;
import com.yt.app.api.v1.vo.SysRoleReBtnPermListVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RolemenuService extends YtIBaseService<Rolemenu, Long> {
	
	YtIPage<Rolemenu> page(Map<String, Object> param);
	
	/**
	 * 获取角色id可访问的菜单ids
	 *
	 * @param roleId 角色id
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	List<Long> getMenuIdsByRoleId(Long roleId);

	/**
	 * 获取角色ids可访问的菜单ids
	 *
	 * @param roleIdList 角色ids
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	List<Long> getMenuIdsByRoleIds(List<Long> roleIdList);

	/**
	 * 保存角色关联菜单ids
	 *
	 * @param params 提交参数
	 * @return void
	 * @author zhengqingya
	 * @date 2020/9/14 11:15
	 */
	void saveRoleMenuIds(SysRoleReMenuSaveDTO params);

	/**
	 * 根据角色id删除角色对应的所有关联菜单
	 *
	 * @param roleId 角色id
	 * @return void
	 * @author zhengqingya
	 * @date 2020/9/10 18:08
	 */
	void delByRoleId(Long roleId);

	/**
	 * 获取租户可访问的菜单ids
	 *
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	List<Integer> getMenuIdList(Integer tenantId);

	/**
	 * 根据菜单id删除关联权限
	 *
	 * @param delMenuIdList 菜单id
	 * @return void
	 * @author zhengqingya
	 * @date 2023/10/8 19:18
	 */
	void delReMenuIdList(List<Integer> delMenuIdList);

	/**
	 * 查询按钮权限
	 *
	 * @return 按钮权限
	 * @author zhengqingya
	 * @date 2023/10/8 19:18
	 */
	List<SysRoleReBtnPermListVO> listRoleReBtnPerm();
}