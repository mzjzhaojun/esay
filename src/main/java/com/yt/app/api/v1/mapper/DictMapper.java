package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Dict;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DictMapper extends YtIBaseMapper<Dict> {
	/**
	 * add
	 * 
	 * @param o TSysdict
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dict.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysdictlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dict.class })
	public Integer batchSava(List<Dict> list);

	/**
	 * update
	 * 
	 * @param o TSysdict
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dict.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysdict
	 */
	@YtRedisCacheAnnotation(classs = Dict.class)
	public Dict get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dict.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Dict.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysdict
	 */
	@YtRedisCacheAnnotation(classs = Dict.class)
	public List<Dict> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysdict
	 */
	@YtRedisCacheAnnotation(classs = Dict.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysdict
	 */
	@YtRedisCacheAnnotation(classs = Dict.class)
	public List<Dict> listByArrayId(long[] id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dict.class })
	public Integer deletebytypid(Long id);

	@YtRedisCacheAnnotation(classs = Dict.class)
	public List<Dict> listByCode(String code);

}