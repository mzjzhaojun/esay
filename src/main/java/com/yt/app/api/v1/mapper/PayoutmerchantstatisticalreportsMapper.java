package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Payoutmerchantstatisticalreports;
import com.yt.app.api.v1.vo.PayoutmerchantstatisticalreportsVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-06-07 23:16:10
 */

public interface PayoutmerchantstatisticalreportsMapper extends YtIBaseMapper<Payoutmerchantstatisticalreports> {
	/**
	 * add
	 * 
	 * @param o Payoutmerchantstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payoutmerchantstatisticalreports.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Payoutmerchantstatisticalreportslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payoutmerchantstatisticalreports.class })
	public Integer batchSava(List<Payoutmerchantstatisticalreports> list);

	/**
	 * update
	 * 
	 * @param o Payoutmerchantstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payoutmerchantstatisticalreports.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payoutmerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Payoutmerchantstatisticalreports.class)
	public Payoutmerchantstatisticalreports get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payoutmerchantstatisticalreports.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Payoutmerchantstatisticalreports.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listPayoutmerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Payoutmerchantstatisticalreports.class)
	public List<Payoutmerchantstatisticalreports> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapPayoutmerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Payoutmerchantstatisticalreports.class)
	public List<PayoutmerchantstatisticalreportsVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listPayoutmerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Payoutmerchantstatisticalreports.class)
	public List<Payoutmerchantstatisticalreports> listByArrayId(long[] id);
}