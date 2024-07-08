package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.ExchangeMerchantaccountorder;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountorderMapper extends YtIBaseMapper<ExchangeMerchantaccountorder> {
	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountorder.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o ExchangeMerchantaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountorder.class })
	public Integer batchSava(List<ExchangeMerchantaccountorder> list);

	/**
	 * update
	 * 
	 * @param o ExchangeMerchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return ExchangeMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public ExchangeMerchantaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listExchangeMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public List<ExchangeMerchantaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapExchangeMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listExchangeMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public List<ExchangeMerchantaccountorder> listByArrayId(long[] id);

	/**
	 * getByOrdernum
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountorder.class)
	public ExchangeMerchantaccountorder getByOrdernum(String ordernum);
}