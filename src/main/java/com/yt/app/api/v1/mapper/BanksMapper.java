package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Banks;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-19 13:11:56
 */

public interface BanksMapper extends YtIBaseMapper<Banks> {
	/**
	 * add
	 * 
	 * @param o Banks
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Banks.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Bankslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Banks.class })
	public Integer batchSava(List<Banks> list);

	/**
	 * update
	 * 
	 * @param o Banks
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Banks.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Banks
	 */
	@YtRedisCacheAnnotation(classs = Banks.class)
	public Banks get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Banks.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Banks.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listBanks
	 */
	@YtRedisCacheAnnotation(classs = Banks.class)
	public List<Banks> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapBanks
	 */
	@YtRedisCacheAnnotation(classs = Banks.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listBanks
	 */
	@YtRedisCacheAnnotation(classs = Banks.class)
	public List<Banks> listByArrayId(long[] id);
}