package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.vo.MerchantaccountorderVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

public interface MerchantaccountorderMapper extends YtIBaseMapper<Merchantaccountorder> {

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public Merchantaccountorder get(Long id);

	/**
	 * add
	 * 
	 * @param o Merchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountorder.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Merchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountorder.class })
	public Integer add(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountorder.class })
	public Integer batchSava(List<Merchantaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountorder.class })
	public Integer put(Object t);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public List<MerchantaccountorderVO> page(Map<String, Object> param);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public Merchantaccountorder getByOrderNum(String ordernum);

	/**
	 * countorder
	 *
	 * @param id id
	 * @return Merchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public Merchantaccountorder countOrder(@Param("userid") Long userid, @Param("dateval") String dateval);

	/**
	 * countsuccessorder
	 *
	 * @param id id
	 * @return Merchantaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountorder.class)
	public Merchantaccountorder countSuccessOrder(@Param("userid") Long userid, @Param("dateval") String dateval);
}