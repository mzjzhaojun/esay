package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.vo.QrcodeaccountorderVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:07:27
 */

public interface QrcodeaccountorderMapper extends YtIBaseMapper<Qrcodeaccountorder> {
	/**
	 * add
	 * 
	 * @param o Qrcodeaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountorder.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Qrcodeaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountorder.class })
	public Integer add(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodeaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountorder.class })
	public Integer batchSava(List<Qrcodeaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Qrcodeaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodeaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public Qrcodeaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodeaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public List<Qrcodeaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodeaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public List<QrcodeaccountorderVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public List<Qrcodeaccountorder> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public Qrcodeaccountorder getByOrderNum(String ordernum);

	/**
	 * countorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public QrcodeaccountorderVO countOrder(@Param("userid") Long userid, @Param("dateval") String dateval);

	/**
	 * countsuccessorder
	 *
	 * @param id id
	 * @return Incomemerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountorder.class)
	public QrcodeaccountorderVO countSuccessOrder(@Param("userid") Long userid, @Param("dateval") String dateval);
}