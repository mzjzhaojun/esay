package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Merchantcustomerbanks;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-01-18 18:43:33
 */

public interface MerchantcustomerbanksMapper extends YtIBaseMapper<Merchantcustomerbanks> {
	/**
	 * add
	 * 
	 * @param o Merchantcustomerbanks
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantcustomerbanks.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantcustomerbankslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantcustomerbanks.class })
	public Integer batchSava(List<Merchantcustomerbanks> list);

	/**
	 * update
	 * 
	 * @param o Merchantcustomerbanks
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantcustomerbanks.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantcustomerbanks
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public Merchantcustomerbanks get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantcustomerbanks.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantcustomerbanks
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public List<Merchantcustomerbanks> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantcustomerbanks
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantcustomerbanks
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public List<Merchantcustomerbanks> listByArrayId(long[] id);

	/**
	 * 
	 * @param accnumber
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Merchantcustomerbanks.class)
	public Merchantcustomerbanks getByAccNumber(String accnumber);
}