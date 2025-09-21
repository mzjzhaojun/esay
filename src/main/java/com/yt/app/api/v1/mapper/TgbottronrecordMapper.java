package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Tgbotgrouprecord;
import com.yt.app.api.v1.entity.Tgbottronrecord;
import com.yt.app.api.v1.vo.TgbottronrecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-05-01 14:00:37
 */

public interface TgbottronrecordMapper extends YtIBaseMapper<Tgbottronrecord> {
	/**
	 * add
	 * 
	 * @param o Tgbottronrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbottronrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgbottronrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbottronrecord.class })
	public Integer batchSava(List<Tgbottronrecord> list);

	/**
	 * update
	 * 
	 * @param o Tgbottronrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbottronrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgbottronrecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbottronrecord.class)
	public Tgbottronrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbottronrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgbottronrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgbottronrecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbottronrecord.class)
	public List<Tgbottronrecord> list(Map<String, Object> param);

	/**
	 * listByType
	 * 
	 * @param id long[]ids
	 * @return listTgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public Tgbottronrecord listByAddress(@Param("tgid") Long tgid, @Param("address") String address);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgbottronrecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbottronrecord.class)
	public List<TgbottronrecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgbottronrecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbottronrecord.class)
	public List<Tgbottronrecord> listByArrayId(long[] id);
}