package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgbotgroup;
import com.yt.app.api.v1.vo.TgbotgroupVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-01 21:36:39
 */

public interface TgbotgroupMapper extends YtIBaseMapper<Tgbotgroup> {
	/**
	 * add
	 * 
	 * @param o Tgbotgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgroup.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgbotgrouplist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgroup.class })
	public Integer batchSava(List<Tgbotgroup> list);

	/**
	 * update
	 * 
	 * @param o Tgbotgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgroup.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgbotgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public Tgbotgroup get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgroup.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgbotgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public List<Tgbotgroup> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgbotgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public List<TgbotgroupVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgbotgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public List<Tgbotgroup> listByArrayId(long[] id);

	/**
	 * getByTgGroupId
	 *
	 * @param id id
	 * @return Tgbotgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgroup.class)
	public Tgbotgroup getByTgGroupId(Long id);

}