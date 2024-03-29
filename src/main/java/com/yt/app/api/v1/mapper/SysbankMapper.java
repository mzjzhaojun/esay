package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Sysbank;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 11:28:36
 */

public interface SysbankMapper extends YtIBaseMapper<Sysbank> {
	/**
	 * add
	 * 
	 * @param o Sysbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysbank.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Sysbanklist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysbank.class })
	public Integer batchSava(List<Sysbank> list);

	/**
	 * update
	 * 
	 * @param o Sysbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysbank.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Sysbank
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public Sysbank get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysbank.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSysbank
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public List<Sysbank> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSysbank
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSysbank
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public List<Sysbank> listByArrayId(long[] id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Sysbank.class)
	public Sysbank getByCode(String code);
}