package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgbotgrouprecord;
import com.yt.app.api.v1.vo.TgbotgrouprecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-04-03 16:45:21
 */

public interface TgbotgrouprecordMapper extends YtIBaseMapper<Tgbotgrouprecord> {
	/**
	 * add
	 * 
	 * @param o Tgbotgrouprecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgrouprecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgbotgrouprecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgrouprecord.class })
	public Integer batchSava(List<Tgbotgrouprecord> list);

	/**
	 * update
	 * 
	 * @param o Tgbotgrouprecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgrouprecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public Tgbotgrouprecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbotgrouprecord.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public List<Tgbotgrouprecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public List<TgbotgrouprecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public List<Tgbotgrouprecord> listByArrayId(long[] id);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgbotgrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgbotgrouprecord.class)
	public List<Tgbotgrouprecord> listByType(Integer type);

}