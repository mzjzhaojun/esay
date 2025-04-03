package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountMapper extends YtIBaseMapper<PayoutMerchantaccount> {
	/**
	 * add
	 * 
	 * @param o Merchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccount.class })
	public Integer batchSava(List<PayoutMerchantaccount> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public PayoutMerchantaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccount.class })
	public Integer delete(Long id);
	
	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccount.class })
	public Integer deleteByUserId(Long userid);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public List<PayoutMerchantaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public List<PayoutMerchantaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public PayoutMerchantaccount getByUserId(Long userid);
	
	
	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccount.class)
	public PayoutMerchantaccount getByMerchantId(Long merchantid);

}