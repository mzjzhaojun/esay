package com.yt.app.api.v1.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yt.app.api.v1.dbo.SysRoleBaseDTO;
import com.yt.app.api.v1.dbo.SysRoleRePermSaveDTO;
import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.Role;
import com.yt.app.api.v1.vo.SysRoleAllPermissionDetailVO;
import com.yt.app.api.v1.vo.SysRoleBaseVO;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.enums.SysRoleCodeEnum;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RoleService extends YtIBaseService<Role, Long> {
	
	YtIPage<Role> page(Map<String, Object> param);
	
	/**
	 * 列表分页
	 *
	 * @param params 查询参数
	 * @return 角色信息
	 * @author zhengqingya
	 * @date 2020/9/10 14:44
	 */
	IPage<SysRoleBaseVO> listPage(SysRoleBaseDTO params);

	/**
	 * 列表
	 *
	 * @param params 查询参数
	 * @return 角色信息
	 * @author zhengqingya
	 * @date 2020/9/10 14:45
	 */
	List<Long> selectListByRoleId(Long roleid);

	/**
	 * 
	 * @param param
	 * @return
	 */
	Integer saverolereperm(SysRoleRePermSaveDTO param);

	/**
	 * 
	 * @param params
	 * @return
	 */
	List<SysRoleBaseVO> list(SysRoleBaseDTO params);

	/**
	 * 详情
	 *
	 * @param roleId 角色ID
	 * @return 角色信息
	 * @author zhengqingya
	 * @date 2020/9/10 14:45
	 */
	Role detail(Long roleId);

	/**
	 * 角色id -> 角色名
	 *
	 * @param roleIdList 角色ids
	 * @return 角色id -> 角色名
	 * @author zhengqingya
	 * @date 2020/9/10 14:44
	 */
	Map<Long, String> mapByRoleIdList(List<Long> roleIdList);

	/**
	 * 树
	 *
	 * @param params 查询参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2020/9/10 14:44
	 */
	List<SysRoleBaseVO> tree(SysRoleBaseDTO params);

	/**
	 * 获取指定角色下的子级角色（包含当前角色）
	 *
	 * @param roleId 角色id
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/09 18:10
	 */
	List<Long> getChildRoleIdList(Long roleId);

	/**
	 * 根据code查询角色ID
	 *
	 * @param sysRoleCodeEnum 角色code
	 * @return 角色ID
	 * @author zhengqingya
	 * @date 2020/9/10 18:03
	 */
	Long getRoleIdByCode(SysRoleCodeEnum sysRoleCodeEnum);

	SysUserPermVO getUserPerm(SysUserPermDTO params);

	SysRoleAllPermissionDetailVO permissionDetail(Long roleId);

	void refreshSuperAdminPerm();
}