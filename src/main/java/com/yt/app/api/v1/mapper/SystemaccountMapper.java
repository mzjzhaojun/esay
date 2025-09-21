package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 19:55:22
 */

public interface SystemaccountMapper extends YtIBaseMapper<Systemaccount> {
	/**
	 * add
	 * 
	 * @param o Systemaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Systemaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer postAndTanantId(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Systemaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer batchSava(List<Systemaccount> list);

	/**
	 * update
	 * 
	 * @param o Systemaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Systemaccount
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public Systemaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSystemaccount
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public List<Systemaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSystemaccount
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSystemaccount
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public List<Systemaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Systemaccount
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public Systemaccount getByTenantId(Long userid);

	/**
	 * 
	 * @param userid
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Systemaccount.class)
	public Systemaccount getByUserId(Long userid);

	/**
	 * 清空每日数据
	 * 
	 * @param id
	 * @return
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccount.class })
	public Integer updatetodayvalue(Long id);
}