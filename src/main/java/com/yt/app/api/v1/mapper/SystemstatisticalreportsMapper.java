package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Systemstatisticalreports;
import com.yt.app.api.v1.vo.SystemstatisticalreportsVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 14:45:16
 */

public interface SystemstatisticalreportsMapper extends YtIBaseMapper<Systemstatisticalreports> {
	/**
	 * add
	 * 
	 * @param o Systemstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemstatisticalreports.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Systemstatisticalreportslist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemstatisticalreports.class })
	public Integer batchSava(List<Systemstatisticalreports> list);

	/**
	 * update
	 * 
	 * @param o Systemstatisticalreports
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemstatisticalreports.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Systemstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Systemstatisticalreports.class)
	public Systemstatisticalreports get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Systemstatisticalreports.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Systemstatisticalreports.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listSystemstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Systemstatisticalreports.class)
	public List<Systemstatisticalreports> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapSystemstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Systemstatisticalreports.class)
	public List<SystemstatisticalreportsVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listSystemstatisticalreports
	 */
	@YtRedisCacheAnnotation(classs = Systemstatisticalreports.class)
	public List<Systemstatisticalreports> listByArrayId(long[] id);
}