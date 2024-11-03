package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 16:03:13
 */

public interface TronMapper extends YtIBaseMapper<Tron> {
	/**
	 * add
	 * 
	 * @param o Tron
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tron.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tronlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tron.class })
	public Integer batchSava(List<Tron> list);

	/**
	 * update
	 * 
	 * @param o Tron
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tron.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tron
	 */
	@YtRedisCacheAnnotation(classs = Tron.class)
	public Tron get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tron.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tron.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTron
	 */
	@YtRedisCacheAnnotation(classs = Tron.class)
	public List<Tron> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTron
	 */
	@YtRedisCacheAnnotation(classs = Tron.class)
	public List<TronVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTron
	 */
	@YtRedisCacheAnnotation(classs = Tron.class)
	public List<Tron> listByArrayId(long[] id);

	@YtRedisCacheAnnotation(classs = Tron.class)
	public Tron getTopOne();

}