package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

public interface IncomemerchantaccountorderMapper extends YtIBaseMapper<Incomemerchantaccountorder> {
	/**
	 * add
	 * 
	 * @param o Incomemerchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountorder.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Incomemerchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountorder.class })
	public Integer add(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Incomemerchantaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountorder.class })
	public Integer batchSava(List<Incomemerchantaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Incomemerchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public Incomemerchantaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Incomemerchantaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listIncomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public List<Incomemerchantaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapIncomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public List<IncomemerchantaccountorderVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listIncomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public List<Incomemerchantaccountorder> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public Incomemerchantaccountorder getByOrderNum(String ordernum);

	/**
	 * countorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public IncomemerchantaccountorderVO countOrder(@Param("userid") Long userid, @Param("dateval") String dateval);

	/**
	 * countsuccessorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Incomemerchantaccountorder.class)
	public IncomemerchantaccountorderVO countSuccessOrder(@Param("userid") Long userid, @Param("dateval") String dateval);
}