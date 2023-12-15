package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface ChannelaccountorderMapper extends YtIBaseMapper<Channelaccountorder> {
	/**
	 * add
	 * 
	 * @param o Channelaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountorder.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Channelaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountorder.class })
	public Integer batchSava(List<Channelaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Channelaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Channelaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public Channelaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannelaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public List<Channelaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapChannelaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listChannelaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public List<Channelaccountorder> listByArrayId(long[] id);

	/**
	 * getByOrdernum
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountorder.class)
	public Channelaccountorder getByOrdernum(String ordernum);
}