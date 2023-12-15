package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.bo.SysScopeDataBO;
import com.yt.app.api.v1.entity.Rolescope;
import com.yt.app.api.v1.vo.SysRoleScopeListVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface RolescopeMapper extends YtIBaseMapper<Rolescope> {
	/**
	 * add
	 * 
	 * @param o TSysrolescope
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysrolescopelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer batchSava(List<Rolescope> list);

	/**
	 * update
	 * 
	 * @param o TSysrolescope
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysrolescope
	 */
	@YtRedisCacheAnnotation(classs = Rolescope.class)
	public Rolescope get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Rolescope.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysrolescope
	 */
	@YtRedisCacheAnnotation(classs = Rolescope.class)
	public List<Rolescope> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysrolescope
	 */
	@YtRedisCacheAnnotation(classs = Rolescope.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysrolescope
	 */
	@YtRedisCacheAnnotation(classs = Rolescope.class)
	public List<Rolescope> listByArrayId(long[] id);

	@YtRedisCacheEvictAnnotation(classs = { Rolescope.class })
	public Integer delByRoleId(Long id);

	/**
	 * 列表
	 *
	 * @param filter 查询过滤参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/18 14:03
	 */
	List<SysRoleScopeListVO> selectListByRoleId(Long roleid);

	/**
	 * 根据角色拿到关联的数据权限
	 *
	 * @param roleIdList 角色ids
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/18 14:03
	 */
	List<SysScopeDataBO> selectScopeListReRoleIdList(@Param("roleIdList") List<Long> roleIdList);

	/**
	 * 删除租户关联权限
	 *
	 * @param delMenuIdList 菜单id
	 * @return void
	 * @author zhengqingya
	 * @date 2023/10/8 19:18
	 */
	void delReMenuIdList(@Param("delMenuIdList") List<Long> delMenuIdList);
}