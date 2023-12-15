package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.dbo.SysScopeDataBaseDTO;
import com.yt.app.api.v1.entity.Scopedata;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-25 17:20:27
 */

public interface ScopedataMapper extends YtIBaseMapper<Scopedata> {
	/**
	 * add
	 * 
	 * @param o TSysscopedata
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Scopedata.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysscopedatalist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Scopedata.class })
	public Integer batchSava(List<Scopedata> list);

	/**
	 * update
	 * 
	 * @param o TSysscopedata
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Scopedata.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysscopedata
	 */
	@YtRedisCacheAnnotation(classs = Scopedata.class)
	public Scopedata get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Scopedata.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Scopedata.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysscopedata
	 */
	@YtRedisCacheAnnotation(classs = Scopedata.class)
	public List<Scopedata> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysscopedata
	 */
	@YtRedisCacheAnnotation(classs = Scopedata.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysscopedata
	 */
	@YtRedisCacheAnnotation(classs = Scopedata.class)
	public List<Scopedata> listByArrayId(long[] id);

	/**
	 * 列表
	 *
	 * @param filter 查询过滤参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/18 14:00
	 */
	List<Scopedata> selectDataList(SysScopeDataBaseDTO filter);
}