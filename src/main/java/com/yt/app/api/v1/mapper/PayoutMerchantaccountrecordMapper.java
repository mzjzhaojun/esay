package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.PayoutMerchantaccountrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountrecordMapper extends YtIBaseMapper<PayoutMerchantaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountrecord.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountrecord.class })
	public Integer postAndTenantid(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountapplyjournallist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountrecord.class })
	public Integer batchSava(List<PayoutMerchantaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountrecord.class)
	public PayoutMerchantaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountrecord.class)
	public List<PayoutMerchantaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountrecord.class)
	public List<PayoutMerchantaccountrecord> listByArrayId(long[] id);
}