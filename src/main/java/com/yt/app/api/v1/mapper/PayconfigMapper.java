package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

public interface PayconfigMapper extends YtIBaseMapper<Payconfig> {
	/**
	 * add
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payconfig.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Payconfiglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payconfig.class })
	public Integer batchSava(List<Payconfig> list);

	/**
	 * update
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payconfig.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payconfig
	 */
	@YtRedisCacheAnnotation(classs = Payconfig.class)
	public Payconfig get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payconfig.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Payconfig.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Payconfig.class)
	public List<Payconfig> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Payconfig.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Payconfig.class)
	public List<Payconfig> listByArrayId(long[] id);

	/**
	 * update
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payconfig.class })
	public Integer putExchange(Double value);

}