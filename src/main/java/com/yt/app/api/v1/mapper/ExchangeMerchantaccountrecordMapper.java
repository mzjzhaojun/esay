package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountrecordMapper extends YtIBaseMapper<ExchangeMerchantaccountrecord> {
	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountrecord.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountrecord.class })
	public Integer postAndTenantid(Object t);

	/**
	 * sava batch
	 * 
	 * @param o ExchangeMerchantaccountapplyjournallist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountrecord.class })
	public Integer batchSava(List<ExchangeMerchantaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return ExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountrecord.class)
	public ExchangeMerchantaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountrecord.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountrecord.class)
	public List<ExchangeMerchantaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountrecord.class)
	public List<ExchangeMerchantaccountrecord> listByArrayId(long[] id);
}