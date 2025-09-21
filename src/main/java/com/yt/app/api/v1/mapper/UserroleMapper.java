package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.yt.app.api.v1.bo.SysUserReRoleIdListBO;
import com.yt.app.api.v1.entity.Userrole;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface UserroleMapper extends YtIBaseMapper<Userrole> {
	/**
	 * add
	 * 
	 * @param o TSysuserrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Userrole.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o TSysuserrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Userrole.class })
	public Integer postAndTanantId(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysuserrolelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Userrole.class })
	public Integer batchSava(List<Userrole> list);

	/**
	 * update
	 * 
	 * @param o TSysuserrole
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Userrole.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysuserrole
	 */
	@YtRedisCacheAnnotation(classs = Userrole.class)
	public Userrole get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Userrole.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Userrole.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysuserrole
	 */
	@YtRedisCacheAnnotation(classs = Userrole.class)
	public List<Userrole> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysuserrole
	 */
	@YtRedisCacheAnnotation(classs = Userrole.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysuserrole
	 */
	@YtRedisCacheAnnotation(classs = Userrole.class)
	public List<Userrole> listByArrayId(long[] id);

	/**
	 * 根据角色id查询关联用户ids
	 *
	 * @param roleId 角色id
	 * @return 用户ids
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	@Select("SELECT user_id FROM t_sys_user_role WHERE role_id = #{roleId}")
	List<Long> listUserId(@Param("roleId") Long roleId);

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
	 * 根据用户id查询关联角色ids
	 *
	 * @param userIdList 用户ids
	 * @return 用户关联角色id
	 * @author zhengqingya
	 * @date 2022/6/14 12:39
	 */
	List<SysUserReRoleIdListBO> selectListByUserIds(@Param("userIdList") List<Long> userIdList);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	public Integer deleteabyUid(Long userid);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	public List<Userrole> getByRoleIdUserId(Userrole ur);

}