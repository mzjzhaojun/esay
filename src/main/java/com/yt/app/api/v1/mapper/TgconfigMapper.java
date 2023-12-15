package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgconfig;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:46:43
 */

public interface TgconfigMapper extends YtIBaseMapper<Tgconfig> {
	/**
	 * add
	 * 
	 * @param o Tgconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgconfig.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgconfiglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgconfig.class })
	public Integer batchSava(List<Tgconfig> list);

	/**
	 * update
	 * 
	 * @param o Tgconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgconfig.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgconfig
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public Tgconfig get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgconfig.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgconfig
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public List<Tgconfig> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgconfig
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgconfig
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public List<Tgconfig> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgconfig
	 */
	@YtRedisCacheAnnotation(classs = Tgconfig.class)
	public Tgconfig getByNmae(String name);

}