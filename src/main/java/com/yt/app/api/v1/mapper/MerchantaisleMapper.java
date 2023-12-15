package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Merchantaisle;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:15:12
 */

public interface MerchantaisleMapper extends YtIBaseMapper<Merchantaisle> {
	/**
	 * add
	 * 
	 * @param o Merchantaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaisle.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaislelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaisle.class })
	public Integer batchSava(List<Merchantaisle> list);

	/**
	 * update
	 * 
	 * @param o Merchantaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaisle.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public Merchantaisle get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaisle.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public List<Merchantaisle> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public List<Merchantaisle> listByArrayId(long[] id);

	@YtRedisCacheAnnotation(classs = Merchantaisle.class)
	public Merchantaisle getByMidAid(@Param("aisleid") Long aisleid, @Param("merchantid") Long merchantid);

}