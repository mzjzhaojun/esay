package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Channelstatisticalreports;
import com.yt.app.api.v1.vo.ChannelstatisticalreportsVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:03:33
 */

public interface ChannelstatisticalreportsMapper extends YtIBaseMapper<Channelstatisticalreports> {
	/**
	 * add
	 * 
	 * @param o Channelstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelstatisticalreports.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Channelstatisticalreportslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelstatisticalreports.class })
	public Integer batchSava(List<Channelstatisticalreports> list);

	/**
	 * update
	 * 
	 * @param o Channelstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelstatisticalreports.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Channelstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Channelstatisticalreports.class)
	public Channelstatisticalreports get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Channelstatisticalreports.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Channelstatisticalreports.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listChannelstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Channelstatisticalreports.class)
	public List<Channelstatisticalreports> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapChannelstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Channelstatisticalreports.class)
	public List<ChannelstatisticalreportsVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listChannelstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Channelstatisticalreports.class)
	public List<Channelstatisticalreports> listByArrayId(long[] id);
}