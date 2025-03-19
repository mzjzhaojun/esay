package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 18:22:46
 */

public interface IncomeMapper extends YtIBaseMapper<Income> {
	/**
	 * add
	 * 
	 * @param o Income
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Income.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Incomelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Income.class })
	public Integer batchSava(List<Income> list);

	/**
	 * update
	 * 
	 * @param o Income
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Income.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Income
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public Income get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Income.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listIncome
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public List<Income> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapIncome
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public List<IncomeVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listIncome
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public List<Income> listByArrayId(long[] id);

	/**
	 * getByOrderNum
	 * 
	 * @param ordernum
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public Income getByOrderNum(String ordernum);

	/**
	 * getByOrderNum
	 * 
	 * @param ordernum
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public Income getByQrcodeOrderNum(String ordernum);

	/**
	 * getByOrderNum
	 * 
	 * @param ordernum
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public Income getByMerchantOrderNum(String ordernum);

	/**
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public List<Income> selectAddlist();

	/**
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public List<Income> selectNotifylist();

	/**
	 * countorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public IncomeVO countOrder(@Param("dateval") String dateval);

	/**
	 * countsuccessorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Income.class)
	public IncomeVO countSuccessOrder(@Param("dateval") String dateval);
	
	
	/**
	 * update
	 * 
	 * @param o Income
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Income.class })
	public Integer updateBlock(Object t);
}