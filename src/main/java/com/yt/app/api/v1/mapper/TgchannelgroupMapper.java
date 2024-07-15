package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-12-27 21:37:32
 */

public interface TgchannelgroupMapper extends YtIBaseMapper<Tgchannelgroup> {
	/**
	 * add
	 * 
	 * @param o Tgchannelgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgroup.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgchannelgrouplist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgroup.class })
	public Integer batchSava(List<Tgchannelgroup> list);

	/**
	 * update
	 * 
	 * @param o Tgchannelgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgroup.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgchannelgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Tgchannelgroup get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgroup.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgchannelgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public List<Tgchannelgroup> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgchannelgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgchannelgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public List<Tgchannelgroup> listByArrayId(long[] id);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Tgchannelgroup getByTgGroupName(String groupname);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Tgchannelgroup getByTgGroupId(Long tgid);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Tgchannelgroup getByChannelId(Long channelId);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Integer updatechannelid(Long id);

	@YtRedisCacheAnnotation(classs = Tgchannelgroup.class)
	public Integer updatetodayvalue(Long Id);

}