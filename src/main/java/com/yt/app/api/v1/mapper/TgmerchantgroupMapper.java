package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.vo.TgmerchantgroupVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

public interface TgmerchantgroupMapper extends YtIBaseMapper<Tgmerchantgroup> {
	/**
	 * add
	 * 
	 * @param o Tgmerchantgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroup.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmerchantgrouplist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroup.class })
	public Integer batchSava(List<Tgmerchantgroup> list);

	/**
	 * update
	 * 
	 * @param o Tgmerchantgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroup.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public Tgmerchantgroup get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroup.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public List<Tgmerchantgroup> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public List<TgmerchantgroupVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public List<Tgmerchantgroup> listByArrayId(long[] id);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public Tgmerchantgroup getByTgGroupName(String groupname);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public Tgmerchantgroup getByTgGroupId(Long tgid);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmerchantgroup.class)
	public Tgmerchantgroup getByMerchantId(Long Id);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmerchantgroup.class })
	public Integer updatetodayvalue(Long Id);
}