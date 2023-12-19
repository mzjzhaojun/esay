package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yt.app.api.v1.entity.Rolemenu;
import com.yt.app.api.v1.entity.Rolescope;
import com.yt.app.api.v1.vo.SysRoleReBtnPermListVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RolemenuMapper extends YtIBaseMapper<Rolemenu> {
	/**
	 * add
	 * 
	 * @param o TSysrolemenu
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolemenu.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysrolemenulist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolemenu.class })
	public Integer batchSava(List<Rolemenu> list);

	/**
	 * update
	 * 
	 * @param o TSysrolemenu
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolemenu.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysrolemenu
	 */
	@YtRedisCacheAnnotation(classs = Rolemenu.class)
	public Rolemenu get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolemenu.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Rolemenu.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysrolemenu
	 */
	@YtRedisCacheAnnotation(classs = Rolemenu.class)
	public List<Rolemenu> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysrolemenu
	 */
	@YtRedisCacheAnnotation(classs = Rolemenu.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysrolemenu
	 */
	@YtRedisCacheAnnotation(classs = Rolemenu.class)
	public List<Rolemenu> listByArrayId(long[] id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolemenu.class })
	public Integer delByRoleId(Long id);

	
	
	
	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer delByMenuId(Long id);

	/**
	 * 获取角色id可访问的菜单ids
	 *
	 * @param roleId 角色id
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	@Select("SELECT menu_id FROM t_sys_role_menu WHERE role_id = #{roleId} ORDER BY menu_id")
	List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 获取租户可访问的菜单ids
	 *
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	@Select("SELECT DISTINCT menu_id FROM t_sys_role_menu WHERE tenant_id = #{tenantId}")
	List<Integer> selectMenuIdList(@Param("tenantId") Integer tenantId);

	/**
	 * 获取角色ids可访问的菜单ids
	 *
	 * @param roleIdList 角色ids
	 * @return 可访问的菜单ids
	 * @author zhengqingya
	 * @date 2020/9/10 18:09
	 */
	List<Long> selectMenuIdsByRoleIds(@Param("roleIdList") List<Long> roleIdList);

	/**
	 * 查询按钮权限
	 *
	 * @return 按钮权限
	 * @author zhengqingya
	 * @date 2023/10/8 19:18
	 */
	List<SysRoleReBtnPermListVO> selectBtnPerm();
}