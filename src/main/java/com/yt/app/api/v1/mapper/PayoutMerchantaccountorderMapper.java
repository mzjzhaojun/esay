package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface PayoutMerchantaccountorderMapper extends YtIBaseMapper<PayoutMerchantaccountorder> {
	/**
	 * add
	 * 
	 * @param o Merchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountorder.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountorder.class })
	public Integer batchSava(List<PayoutMerchantaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public PayoutMerchantaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { PayoutMerchantaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public List<PayoutMerchantaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public List<PayoutMerchantaccountorder> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public List<PayoutMerchantaccountorder> listByArrayId(long[] id);

	/**
	 * getByOrdernum
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = PayoutMerchantaccountorder.class)
	public PayoutMerchantaccountorder getByOrdernum(String ordernum);
}