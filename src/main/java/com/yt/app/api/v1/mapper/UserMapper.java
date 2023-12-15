package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysUserPermDTO;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.SysUserPermVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 16:55:15
 */

public interface UserMapper extends YtIBaseMapper<User> {
	/**
	 * add
	 * 
	 * @param o TSysuser
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { User.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o TSysuser
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { User.class })
	public Integer postAndTanantId(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysuserlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { User.class })
	public Integer batchSava(List<User> list);

	/**
	 * update
	 * 
	 * @param o TSysuser
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { User.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysuser
	 */
	@YtRedisCacheAnnotation(classs = User.class)
	public User get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { User.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = User.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysuser
	 */
	@YtRedisCacheAnnotation(classs = User.class)
	public List<User> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysuser
	 */
	@YtRedisCacheAnnotation(classs = User.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysuser
	 */
	@YtRedisCacheAnnotation(classs = User.class)
	public List<User> listByArrayId(long[] id);

	/**
	 * 查询用户信息
	 *
	 * @param filter 过滤参数
	 * @return 用户信息
	 * @author zhengqingya
	 * @date 2020/9/21 16:18
	 */
	SysUserPermVO selectUserPerm(SysUserPermDTO params);

	/**
	 * 测试sql注入问题
	 *
	 * @param username 用户名
	 * @return void
	 * @author zhengqingya
	 * @date 2023/2/28 11:07
	 */
	User getByUserName(String username);

	/**
	 * 查询部门id关联的用户数
	 *
	 * @param deptId 部门id
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/10 15:08
	 */
	long selectUserNumByDeptId(Integer deptId);

}