package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.ExchangeMerchantaccountapplyjournal;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface ExchangeMerchantaccountrecordMapper extends YtIBaseMapper<ExchangeMerchantaccountapplyjournal> {
	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountapplyjournal.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountapplyjournal.class })
	public Integer postAndTenantid(Object t);

	/**
	 * sava batch
	 * 
	 * @param o ExchangeMerchantaccountapplyjournallist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountapplyjournal.class })
	public Integer batchSava(List<ExchangeMerchantaccountapplyjournal> list);

	/**
	 * update
	 * 
	 * @param o ExchangeMerchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountapplyjournal.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return ExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountapplyjournal.class)
	public ExchangeMerchantaccountapplyjournal get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { ExchangeMerchantaccountapplyjournal.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountapplyjournal.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountapplyjournal.class)
	public List<ExchangeMerchantaccountapplyjournal> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountapplyjournal.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listExchangeMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = ExchangeMerchantaccountapplyjournal.class)
	public List<ExchangeMerchantaccountapplyjournal> listByArrayId(long[] id);
}