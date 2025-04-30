package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.api.v1.vo.SysokxVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-30 13:30:55
 */

public interface SysokxMapper extends YtIBaseMapper<Sysokx> {
	/**
	 * add
	 * 
	 * @param o Sysokx
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysokx.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Sysokxlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysokx.class })
	public Integer batchSava(List<Sysokx> list);

	/**
	 * update
	 * 
	 * @param o Sysokx
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysokx.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Sysokx
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public Sysokx get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysokx.class })
	public Integer delete(Long id);

	/**
	 * deleteAll
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysokx.class })
	@InterceptorIgnore(blockAttack = "true")
	public Integer deleteAll();

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSysokx
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public List<Sysokx> list(Map<String, Object> param);

	/**
	 * listTop
	 * 
	 * @param param map
	 * @return listSysokx
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public List<Sysokx> listTop(String type);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSysokx
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public List<SysokxVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSysokx
	 */
	@YtRedisCacheAnnotation(classs = Sysokx.class)
	public List<Sysokx> listByArrayId(long[] id);
}