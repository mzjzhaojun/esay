package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgmerchantgroupmessage;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-26 13:20:46
 */

public interface TgmerchantgroupmessageMapper extends YtIBaseMapper<Tgmerchantgroupmessage> {
	/**
	 * add
	 * 
	 * @param o Tgmerchantgroupmessage
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroupmessage.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmerchantgroupmessagelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroupmessage.class })
	public Integer batchSava(List<Tgmerchantgroupmessage> list);

	/**
	 * update
	 * 
	 * @param o Tgmerchantgroupmessage
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroupmessage.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmerchantgroupmessage
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroupmessage.class)
	public Tgmerchantgroupmessage get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroupmessage.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroupmessage.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmerchantgroupmessage
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroupmessage.class)
	public List<Tgmerchantgroupmessage> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmerchantgroupmessage
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroupmessage.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmerchantgroupmessage
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroupmessage.class)
	public List<Tgmerchantgroupmessage> listByArrayId(long[] id);
}