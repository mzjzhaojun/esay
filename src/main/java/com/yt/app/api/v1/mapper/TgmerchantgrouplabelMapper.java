package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgmerchantgrouplabel;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

public interface TgmerchantgrouplabelMapper extends YtIBaseMapper<Tgmerchantgrouplabel> {
	/**
	 * add
	 * 
	 * @param o Tgmerchantgrouplabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgrouplabel.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmerchantgrouplabellist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgrouplabel.class })
	public Integer batchSava(List<Tgmerchantgrouplabel> list);

	/**
	 * update
	 * 
	 * @param o Tgmerchantgrouplabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgrouplabel.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmerchantgrouplabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgrouplabel.class)
	public Tgmerchantgrouplabel get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgrouplabel.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgrouplabel.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmerchantgrouplabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgrouplabel.class)
	public List<Tgmerchantgrouplabel> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmerchantgrouplabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgrouplabel.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmerchantgrouplabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgrouplabel.class)
	public List<Tgmerchantgrouplabel> listByArrayId(long[] id);
}