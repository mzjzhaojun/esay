package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.IncomeMerchantstatisticalreports;
import com.yt.app.api.v1.vo.IncomeMerchantstatisticalreportsVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 12:01:51
 */

public interface IncomeMerchantstatisticalreportsMapper extends YtIBaseMapper<IncomeMerchantstatisticalreports> {
	/**
	 * add
	 * 
	 * @param o Merchantstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { IncomeMerchantstatisticalreports.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantstatisticalreportslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { IncomeMerchantstatisticalreports.class })
	public Integer batchSava(List<IncomeMerchantstatisticalreports> list);

	/**
	 * update
	 * 
	 * @param o Merchantstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { IncomeMerchantstatisticalreports.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = IncomeMerchantstatisticalreports.class)
	public IncomeMerchantstatisticalreports get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { IncomeMerchantstatisticalreports.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = IncomeMerchantstatisticalreports.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = IncomeMerchantstatisticalreports.class)
	public List<IncomeMerchantstatisticalreports> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = IncomeMerchantstatisticalreports.class)
	public List<IncomeMerchantstatisticalreportsVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = IncomeMerchantstatisticalreports.class)
	public List<IncomeMerchantstatisticalreports> listByArrayId(long[] id);
}