package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.vo.IncomemerchantaccountVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 23:02:54
 */

public interface IncomemerchantaccountMapper extends YtIBaseMapper<Incomemerchantaccount> {
	/**
	 * add
	 * 
	 * @param o Incomemerchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Incomemerchantaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccount.class })
	public Integer batchSava(List<Incomemerchantaccount> list);

	/**
	 * update
	 * 
	 * @param o Incomemerchantaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public Incomemerchantaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccount.class })
	public Integer delById(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccount.class })
	public Integer deleteByUserId(Long userid);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listIncomemerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public List<Incomemerchantaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapIncomemerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public List<IncomemerchantaccountVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listIncomemerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public List<Incomemerchantaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public Incomemerchantaccount getByUserId(Long userid);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccount.class)
	public Incomemerchantaccount getByMerchantId(Long id);
}