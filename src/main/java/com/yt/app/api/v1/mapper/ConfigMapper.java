package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Config;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-02 20:38:10
 */

public interface ConfigMapper extends YtIBaseMapper<Config> {
	/**
	 * add
	 * 
	 * @param o TSysconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Config.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysconfiglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Config.class })
	public Integer batchSava(List<Config> list);

	/**
	 * update
	 * 
	 * @param o TSysconfig
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Config.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysconfig
	 */
	@YtRedisCacheAnnotation(classs = Config.class)
	public Config get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Config.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Config.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysconfig
	 */
	@YtRedisCacheAnnotation(classs = Config.class)
	public List<Config> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysconfig
	 */
	@YtRedisCacheAnnotation(classs = Config.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysconfig
	 */
	@YtRedisCacheAnnotation(classs = Config.class)
	public List<Config> listByArrayId(long[] id);
}