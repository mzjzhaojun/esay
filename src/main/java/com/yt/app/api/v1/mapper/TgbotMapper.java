package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgbot;
import com.yt.app.api.v1.vo.TgbotVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */

public interface TgbotMapper extends YtIBaseMapper<Tgbot> {
	/**
	 * add
	 * 
	 * @param o Tgbot
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbot.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgbotlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbot.class })
	public Integer batchSava(List<Tgbot> list);

	/**
	 * update
	 * 
	 * @param o Tgbot
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbot.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgbot
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public Tgbot get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgbot.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgbot
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public List<Tgbot> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgbot
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public List<TgbotVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgbot
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public List<Tgbot> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgbot
	 */
	@YtRedisCacheAnnotation(classs = Tgbot.class)
	public Tgbot getByToken(String token);
}