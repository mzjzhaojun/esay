package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Channelaccount;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ChannelaccountMapper extends YtIBaseMapper<Channelaccount> {
	/**
	 * add
	 * 
	 * @param o Channelaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Channelaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccount.class })
	public Integer batchSava(List<Channelaccount> list);

	/**
	 * update
	 * 
	 * @param o Channelaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Channelaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public Channelaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccount.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannelaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public List<Channelaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapChannelaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listChannelaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public List<Channelaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccount.class)
	public Channelaccount getByUserId(Long userid);
}