package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tronaddress;
import com.yt.app.api.v1.vo.TronaddressVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 01:44:43
 */

public interface TronaddressMapper extends YtIBaseMapper<Tronaddress> {
	/**
	 * add
	 * 
	 * @param o Tronaddress
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronaddress.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tronaddresslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronaddress.class })
	public Integer batchSava(List<Tronaddress> list);

	/**
	 * update
	 * 
	 * @param o Tronaddress
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronaddress.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tronaddress
	 */
	@YtRedisCacheAnnotation(classs = Tronaddress.class)
	public Tronaddress get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronaddress.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tronaddress.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTronaddress
	 */
	@YtRedisCacheAnnotation(classs = Tronaddress.class)
	public List<Tronaddress> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTronaddress
	 */
	@YtRedisCacheAnnotation(classs = Tronaddress.class)
	public List<TronaddressVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTronaddress
	 */
	@YtRedisCacheAnnotation(classs = Tronaddress.class)
	public List<Tronaddress> listByArrayId(long[] id);
}