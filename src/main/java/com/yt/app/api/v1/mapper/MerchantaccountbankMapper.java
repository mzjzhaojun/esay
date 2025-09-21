package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Merchantaccountbank;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:42:46
 */

public interface MerchantaccountbankMapper extends YtIBaseMapper<Merchantaccountbank> {
	/**
	 * add
	 * 
	 * @param o Merchantaccountbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountbank.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountbanklist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountbank.class })
	public Integer batchSava(List<Merchantaccountbank> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccountbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountbank.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public Merchantaccountbank get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountbank.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public List<Merchantaccountbank> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public List<Merchantaccountbank> listByArrayId(long[] id);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountbank.class)
	public List<Merchantaccountbank> listByUserid(Long userid);

}