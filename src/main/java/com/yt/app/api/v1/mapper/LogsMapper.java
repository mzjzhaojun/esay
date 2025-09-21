package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Logs;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-31 13:31:43
 */

public interface LogsMapper extends YtIBaseMapper<Logs> {
	/**
	 * add
	 * 
	 * @param o Logs
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Logs.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Logslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Logs.class })
	public Integer batchSava(List<Logs> list);

	/**
	 * update
	 * 
	 * @param o Logs
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Logs.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Logs
	 */
	@YtRedisCacheAnnotation(classs = Logs.class)
	public Logs get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Logs.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Logs.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listLogs
	 */
	@YtRedisCacheAnnotation(classs = Logs.class)
	public List<Logs> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapLogs
	 */
	@YtRedisCacheAnnotation(classs = Logs.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listLogs
	 */
	@YtRedisCacheAnnotation(classs = Logs.class)
	public List<Logs> listByArrayId(long[] id);
}