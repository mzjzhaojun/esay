package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Merchantqrcodeaisle;
import com.yt.app.api.v1.vo.MerchantqrcodeaisleVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 16:58:38
 */

public interface MerchantqrcodeaisleMapper extends YtIBaseMapper<Merchantqrcodeaisle> {
	/**
	 * add
	 * 
	 * @param o Merchantqrcodeaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantqrcodeaisle.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantqrcodeaislelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantqrcodeaisle.class })
	public Integer batchSava(List<Merchantqrcodeaisle> list);

	/**
	 * update
	 * 
	 * @param o Merchantqrcodeaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantqrcodeaisle.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantqrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public Merchantqrcodeaisle get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantqrcodeaisle.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantqrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public List<Merchantqrcodeaisle> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantqrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public List<MerchantqrcodeaisleVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantqrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public List<Merchantqrcodeaisle> listByArrayId(long[] id);

	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public Merchantqrcodeaisle getByMidAid(@Param("qrcodeaisleid") Long qrcodeaisleid, @Param("merchantid") Long merchantid);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaisle
	 */
	@YtRedisCacheAnnotation(classs = Merchantqrcodeaisle.class)
	public List<Merchantqrcodeaisle> getByMid(Long mid);

	/**
	 * deleteByQrcodeaisleId
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantqrcodeaisle.class })
	public Integer deleteByQrcodeaisleId(Long id);

}