package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.bo.SysScopeDataBO;
import com.yt.app.api.v1.dbo.SysRoleReScopeSaveDTO;
import com.yt.app.api.v1.entity.Rolescope;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RolescopeService extends YtIBaseService<Rolescope, Long> {

	YtIPage<Rolescope> page(Map<String, Object> param);

	/**
	 * 根据角色id拿到关联的数据权限
	 *
	 * @param roleId 角色ID
	 * @return 数据权限ids
	 * @author zhengqingya
	 * @date 2020/9/10 15:01
	 */
	List<Long> getScopeIdListByRoleId(Long roleId);

	/**
	 * 根据角色拿到关联的数据权限
	 *
	 * @param roleIdList 角色ids
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/18 14:03
	 */
	List<SysScopeDataBO> getScopeListReRoleIdList(List<Long> roleIdList);

	/**
	 * 保存
	 *
	 * @param params 保存参数
	 * @return void
	 * @author zhengqingya
	 * @date 2023/10/18 14:03
	 */
	void saveScopeData(SysRoleReScopeSaveDTO params);

	/**
	 * 根据角色id删除角色对应的所有关联权限
	 *
	 * @param roleId 角色id
	 * @return void
	 * @author zhengqingya
	 * @date 2020/9/10 18:08
	 */
	void delByRoleId(Long roleId);

	/**
	 * 根据菜单id删除关联权限
	 *
	 * @param delMenuIdList 菜单id
	 * @return void
	 * @author zhengqingya
	 * @date 2023/10/8 19:18
	 */
	void delReMenuIdList(List<Long> delMenuIdList);

	/**
	 * 根据数据权限id删除关联权限
	 *
	 * @param scopeIdList 数据权限id
	 * @return void
	 * @author zhengqingya
	 * @date 2020/9/10 18:08
	 */
	void delByScopeIdList(List<Long> scopeIdList);
}