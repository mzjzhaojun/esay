package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */

public interface AisleMapper extends YtIBaseMapper<Aisle> {
	/**
	 * add
	 * 
	 * @param o Aisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aisle.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Aislelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aisle.class })
	public Integer batchSava(List<Aisle> list);

	/**
	 * update
	 * 
	 * @param o Aisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aisle.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Aisle
	 */
	@YtRedisCacheAnnotation(classs = Aisle.class)
	public Aisle get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aisle.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Aisle.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAisle
	 */
	@YtRedisCacheAnnotation(classs = Aisle.class)
	public List<Aisle> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAisle
	 */
	@YtRedisCacheAnnotation(classs = Aisle.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAisle
	 */
	@YtRedisCacheAnnotation(classs = Aisle.class)
	public List<Aisle> listByArrayId(long[] id);
}