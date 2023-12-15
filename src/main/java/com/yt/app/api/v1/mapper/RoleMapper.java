package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yt.app.api.v1.dbo.SysRoleBaseDTO;
import com.yt.app.api.v1.entity.Role;
import com.yt.app.api.v1.vo.SysRoleBaseVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RoleMapper extends YtIBaseMapper<Role> {
	/**
	 * add
	 * 
	 * @param o TSysrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Role.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o TSysrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Role.class })
	public Integer postAndTanantId(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysrolelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Role.class })
	public Integer batchSava(List<Role> list);

	/**
	 * update
	 * 
	 * @param o TSysrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Role.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysrole
	 */
	@YtRedisCacheAnnotation(classs = Role.class)
	public Role get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Role.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Role.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysrole
	 */
	@YtRedisCacheAnnotation(classs = Role.class)
	public List<Role> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysrole
	 */
	@YtRedisCacheAnnotation(classs = Role.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysrole
	 */
	@YtRedisCacheAnnotation(classs = Role.class)
	public List<Role> listByArrayId(long[] id);

	/**
	 * 列表分页
	 *
	 * @param page:
	 * @param filter: 过滤参数
	 * @return 角色信息
	 * @author zhengqingya
	 * @date 2020/9/10 18:07
	 */

	IPage<SysRoleBaseVO> selectDataList(IPage<SysRoleBaseVO> page, @Param("filter") SysRoleBaseDTO filter);

	/**
	 * 列表
	 *
	 * @param filter: 过滤参数
	 * @return 角色信息
	 * @author zhengqingya
	 * @date 2020/9/10 18:08
	 */
	List<SysRoleBaseVO> selectDataList(@Param("filter") SysRoleBaseDTO filter);

	/**
	 * 根据角色编码查询角色ID
	 *
	 * @param code 角色编码
	 * @return 角色ID
	 * @author zhengqingya
	 * @date 2020/9/10 18:03
	 */
	Long selectRoleIdByCode(String code);
}