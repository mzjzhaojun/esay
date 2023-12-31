package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Systemcapitalrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */

public interface SystemcapitalrecordMapper extends YtIBaseMapper<Systemcapitalrecord> {
	/**
	 * add
	 * 
	 * @param o Systemcapitalrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemcapitalrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Systemcapitalrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemcapitalrecord.class })
	public Integer batchSava(List<Systemcapitalrecord> list);

	/**
	 * update
	 * 
	 * @param o Systemcapitalrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemcapitalrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Systemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemcapitalrecord.class)
	public Systemcapitalrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemcapitalrecord.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Systemcapitalrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemcapitalrecord.class)
	public List<Systemcapitalrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemcapitalrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemcapitalrecord.class)
	public List<Systemcapitalrecord> listByArrayId(long[] id);
}