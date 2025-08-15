package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgfootballgroup;
import com.yt.app.api.v1.vo.TgfootballgroupVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-15 17:34:22
 */

public interface TgfootballgroupMapper extends YtIBaseMapper<Tgfootballgroup> {
	/**
	 * add
	 * 
	 * @param o Tgfootballgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgfootballgroup.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgfootballgrouplist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgfootballgroup.class })
	public Integer batchSava(List<Tgfootballgroup> list);

	/**
	 * update
	 * 
	 * @param o Tgfootballgroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgfootballgroup.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgfootballgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public Tgfootballgroup get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgfootballgroup.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgfootballgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public List<Tgfootballgroup> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgfootballgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public List<TgfootballgroupVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgfootballgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public List<Tgfootballgroup> listByArrayId(long[] id);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmerchantgroup
	 */
	@YtRedisCacheAnnotation(classs = Tgfootballgroup.class)
	public Tgfootballgroup getByTgGroupId(Long tgid);
}