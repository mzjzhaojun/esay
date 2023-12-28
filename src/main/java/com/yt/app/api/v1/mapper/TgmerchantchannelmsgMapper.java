package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-28 15:01:59
 */

public interface TgmerchantchannelmsgMapper extends YtIBaseMapper<Tgmerchantchannelmsg> {
	/**
	 * add
	 * 
	 * @param o Tgmerchantchannelmsg
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantchannelmsg.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmerchantchannelmsglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantchannelmsg.class })
	public Integer batchSava(List<Tgmerchantchannelmsg> list);

	/**
	 * update
	 * 
	 * @param o Tgmerchantchannelmsg
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantchannelmsg.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmerchantchannelmsg
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantchannelmsg.class)
	public Tgmerchantchannelmsg get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantchannelmsg.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantchannelmsg.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmerchantchannelmsg
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantchannelmsg.class)
	public List<Tgmerchantchannelmsg> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmerchantchannelmsg
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantchannelmsg.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmerchantchannelmsg
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantchannelmsg.class)
	public List<Tgmerchantchannelmsg> listByArrayId(long[] id);
}