package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysProvinceCityAreaTreeDTO;
import com.yt.app.api.v1.entity.Provincecityarea;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-03 19:50:02
 */

public interface ProvincecityareaMapper extends YtIBaseMapper<Provincecityarea> {
	/**
	 * add
	 * 
	 * @param o TSysprovincecityarea
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Provincecityarea.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysprovincecityarealist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Provincecityarea.class })
	public Integer batchSava(List<Provincecityarea> list);

	/**
	 * update
	 * 
	 * @param o TSysprovincecityarea
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Provincecityarea.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysprovincecityarea
	 */
	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	public Provincecityarea get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Provincecityarea.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysprovincecityarea
	 */
	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	public List<Provincecityarea> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysprovincecityarea
	 */
	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysprovincecityarea
	 */
	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	public List<Provincecityarea> listByArrayId(long[] id);

	@YtRedisCacheAnnotation(classs = Provincecityarea.class)
	List<Provincecityarea> selectDataList(SysProvinceCityAreaTreeDTO filter);
}