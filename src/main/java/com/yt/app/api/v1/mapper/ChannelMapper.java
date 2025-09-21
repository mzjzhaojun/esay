package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-12 10:55:20
 */

public interface ChannelMapper extends YtIBaseMapper<Channel> {
	/**
	 * add
	 * 
	 * @param o Channel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channel.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Channellist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channel.class })
	public Integer batchSava(List<Channel> list);

	/**
	 * update
	 * 
	 * @param o Channel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channel.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Channel
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public Channel get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channel.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannel
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public List<Channel> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapChannel
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listChannel
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public List<Channel> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public Channel getByUserId(Long userid);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channel.class })
	public Integer updatetodayvalue(Long id);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannel
	 */
	@YtRedisCacheAnnotation(classs = Channel.class)
	public List<Channel> getSynList();

}