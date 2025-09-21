package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Dicttype;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DicttypeMapper extends YtIBaseMapper<Dicttype> {
	/**
	 * add
	 * 
	 * @param o TSysdicttype
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dicttype.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysdicttypelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dicttype.class })
	public Integer batchSava(List<Dicttype> list);

	/**
	 * update
	 * 
	 * @param o TSysdicttype
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dicttype.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysdicttype
	 */
	@YtRedisCacheAnnotation(classs = Dicttype.class)
	public Dicttype get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dicttype.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Dicttype.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysdicttype
	 */
	@YtRedisCacheAnnotation(classs = Dicttype.class)
	public List<Dicttype> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysdicttype
	 */
	@YtRedisCacheAnnotation(classs = Dicttype.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysdicttype
	 */
	@YtRedisCacheAnnotation(classs = Dicttype.class)
	public List<Dicttype> listByArrayId(long[] id);
}