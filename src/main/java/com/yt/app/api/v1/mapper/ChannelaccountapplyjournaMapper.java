package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Channelaccountapplyjourna;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:44:01
 */

public interface ChannelaccountapplyjournaMapper extends YtIBaseMapper<Channelaccountapplyjourna> {
	/**
	 * add
	 * 
	 * @param o Channelaccountapplyjourna
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountapplyjourna.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Channelaccountapplyjournalist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountapplyjourna.class })
	public Integer batchSava(List<Channelaccountapplyjourna> list);

	/**
	 * update
	 * 
	 * @param o Channelaccountapplyjourna
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountapplyjourna.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Channelaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountapplyjourna.class)
	public Channelaccountapplyjourna get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelaccountapplyjourna.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountapplyjourna.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannelaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountapplyjourna.class)
	public List<Channelaccountapplyjourna> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapChannelaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountapplyjourna.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listChannelaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Channelaccountapplyjourna.class)
	public List<Channelaccountapplyjourna> listByArrayId(long[] id);
}