package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgmerchantlabel;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

public interface TgmerchantlabelMapper extends YtIBaseMapper<Tgmerchantlabel> {
	/**
	 * add
	 * 
	 * @param o Tgmerchantlabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantlabel.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmerchantlabellist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantlabel.class })
	public Integer batchSava(List<Tgmerchantlabel> list);

	/**
	 * update
	 * 
	 * @param o Tgmerchantlabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantlabel.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmerchantlabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantlabel.class)
	public Tgmerchantlabel get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantlabel.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantlabel.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmerchantlabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantlabel.class)
	public List<Tgmerchantlabel> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmerchantlabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantlabel.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmerchantlabel
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantlabel.class)
	public List<Tgmerchantlabel> listByArrayId(long[] id);
}