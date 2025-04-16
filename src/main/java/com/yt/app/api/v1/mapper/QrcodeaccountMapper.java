package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.vo.QrcodeaccountVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

public interface QrcodeaccountMapper extends YtIBaseMapper<Qrcodeaccount> {
	/**
	 * add
	 * 
	 * @param o Qrcodeaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodeaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccount.class })
	public Integer batchSava(List<Qrcodeaccount> list);

	/**
	 * update
	 * 
	 * @param o Qrcodeaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodeaccount
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public Qrcodeaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccount.class })
	public Integer delete(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccount.class })
	public Integer deleteByUserId(Long userid);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodeaccount
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public List<Qrcodeaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodeaccount
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public List<QrcodeaccountVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaccount
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public List<Qrcodeaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccount.class)
	public Qrcodeaccount getByUserId(Long userid);
}