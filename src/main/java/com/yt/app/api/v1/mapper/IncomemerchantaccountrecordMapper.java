package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Incomemerchantaccountrecord;
import com.yt.app.api.v1.vo.IncomemerchantaccountrecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

public interface IncomemerchantaccountrecordMapper extends YtIBaseMapper<Incomemerchantaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Incomemerchantaccountrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Incomemerchantaccountrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountrecord.class })
	public Integer batchSava(List<Incomemerchantaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Incomemerchantaccountrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountrecord.class)
	public Incomemerchantaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listIncomemerchantaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountrecord.class)
	public List<Incomemerchantaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapIncomemerchantaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountrecord.class)
	public List<IncomemerchantaccountrecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listIncomemerchantaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountrecord.class)
	public List<Incomemerchantaccountrecord> listByArrayId(long[] id);
}