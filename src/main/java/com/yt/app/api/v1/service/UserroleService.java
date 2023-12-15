package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysUserRoleSaveDTO;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.base.YtIBaseService;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface UserroleService extends YtIBaseService<Userrole, Long> {

	/**
	 * 新增或更新
	 *
	 * @param params 提交参数
	 * @return void
	 * @author zhengqingya
	 * @date 2020/9/10 14:29
	 */
	void addOrUpdateData(SysUserRoleSaveDTO params);

	/**
	 * 根据角色id查询关联用户ids
	 *
	 * @param roleId 角色id
	 * @return 用户ids
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	List<Long> listUserId(Long roleId);

	/**
	 * 根据用户id查询关联角色ids
	 *
	 * @param userId 用户id
	 * @return 角色ids
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	List<Long> listRoleId(Long userId);

	/**
	 * 根据用户ids查询关联角色ids
	 *
	 * @param userIdList 用户ids
	 * @return 用户id -> 角色ids
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	Map<Long, List<Long>> mapRoleId(List<Long> userIdList);

	/**
	 * 删除用户id关联角色ids
	 *
	 * @param userId 用户id
	 * @return void
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	void delByUserId(Long userId);
}