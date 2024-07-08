package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.ExchangeMerchantaccount;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountMapper extends YtIBaseMapper<ExchangeMerchantaccount> {
	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o ExchangeMerchantaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccount.class })
	public Integer batchSava(List<ExchangeMerchantaccount> list);

	/**
	 * update
	 * 
	 * @param o ExchangeMerchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return ExchangeMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public ExchangeMerchantaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccount.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listExchangeMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public List<ExchangeMerchantaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapExchangeMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listExchangeMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public List<ExchangeMerchantaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return ExchangeMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccount.class)
	public ExchangeMerchantaccount getByUserId(Long userid);

}