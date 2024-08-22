package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.vo.QrcodeaisleVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */

public interface QrcodeaisleMapper extends YtIBaseMapper<Qrcodeaisle> {
	/**
	 * add
	 * 
	 * @param o Qrcodeaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisle.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodeaislelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisle.class })
	public Integer batchSava(List<Qrcodeaisle> list);

	/**
	 * update
	 * 
	 * @param o Qrcodeaisle
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisle.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisle.class)
	public Qrcodeaisle get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisle.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisle.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisle.class)
	public List<Qrcodeaisle> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisle.class)
	public List<QrcodeaisleVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaisle
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisle.class)
	public List<Qrcodeaisle> listByArrayId(long[] id);
}