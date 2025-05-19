package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.entity.TSysconfig;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

public interface SysconfigMapper extends YtIBaseMapper<Sysconfig> {
	/**
	 * add
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Payconfiglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer batchSava(List<Sysconfig> list);

	/**
	 * update
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payconfig
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public Sysconfig get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public List<Sysconfig> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listPayconfig
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public List<Sysconfig> listByArrayId(long[] id);

	/**
	 * update
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer putUsdtExchange(Double value);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payconfig
	 */
	@YtRedisCacheAnnotation(classs = Sysconfig.class)
	public Sysconfig getByName(String name);

	/**
	 * update
	 * 
	 * @param o Payconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Sysconfig.class })
	public Integer putUsdtToTrxExchange(Double value);
	
	
	public List<TSysconfig> listSysconfig();

}