package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysMenuTreeDTO;
import com.yt.app.api.v1.entity.Menu;
import com.yt.app.api.v1.vo.SysMenuTreeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface MenuMapper extends YtIBaseMapper<Menu> {
	/**
	 * add
	 * 
	 * @param o TSysmenu
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Menu.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysmenulist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Menu.class })
	public Integer batchSava(List<Menu> list);

	/**
	 * update
	 * 
	 * @param o TSysmenu
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Menu.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysmenu
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public Menu get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Menu.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysmenu
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<Menu> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysmenu
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysmenu
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<Menu> listByArrayId(long[] id);

	/**
	 * 
	 * @param param
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<SysMenuTreeVO> selectMenuTree(SysMenuTreeDTO param);

	/**
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<Long> selectAllMenuId();

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysmenu
	 */
	@YtRedisCacheAnnotation(classs = Menu.class)
	public List<SysMenuTreeVO> getByParentId(Long parentid);

}