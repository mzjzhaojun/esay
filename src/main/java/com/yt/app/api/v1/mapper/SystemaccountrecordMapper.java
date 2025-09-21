package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Systemaccountrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 20:07:25
 */

public interface SystemaccountrecordMapper extends YtIBaseMapper<Systemaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Systemcapitalrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccountrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Systemcapitalrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccountrecord.class })
	public Integer batchSava(List<Systemaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Systemcapitalrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Systemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemaccountrecord.class)
	public Systemaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemaccountrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Systemaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemaccountrecord.class)
	public List<Systemaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemaccountrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSystemcapitalrecord
	 */
	@YtRedisCacheAnnotation(classs = Systemaccountrecord.class)
	public List<Systemaccountrecord> listByArrayId(long[] id);
}