package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-11 15:34:24
 */

public interface MerchantMapper extends YtIBaseMapper<Merchant> {
	/**
	 * add
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer batchSava(List<Merchant> list);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchant
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public Merchant get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchant
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public List<Merchant> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchant
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchant
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public List<Merchant> listByArrayId(long[] id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer removeagent(Long id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public Merchant getByUserId(Long userid);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public Merchant getByCode(String code);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer putagent(Merchant t);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchant
	 */
	@YtRedisCacheAnnotation(classs = Merchant.class)
	public List<Merchant> getListAll(Map<String, Object> param);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchant.class })
	public Integer updatetodayvalue(Long id);
}