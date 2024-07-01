package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.vo.AislechannelVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-13 10:16:34
 */

public interface AislechannelMapper extends YtIBaseMapper<Aislechannel> {
	/**
	 * add
	 * 
	 * @param o Aislechannel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aislechannel.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Aislechannellist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aislechannel.class })
	public Integer batchSava(List<Aislechannel> list);

	/**
	 * update
	 * 
	 * @param o Aislechannel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aislechannel.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Aislechannel
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public Aislechannel get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Aislechannel.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAislechannel
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public List<Aislechannel> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTwitter
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public List<AislechannelVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAislechannel
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public List<Aislechannel> listByArrayId(long[] id);

	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public Aislechannel getByAidCid(@Param("aisleid") Long aisleid, @Param("channelid") Long channelid);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAislechannel
	 */
	@YtRedisCacheAnnotation(classs = Aislechannel.class)
	public List<Aislechannel> getByAisleId(Long aisleId);

}