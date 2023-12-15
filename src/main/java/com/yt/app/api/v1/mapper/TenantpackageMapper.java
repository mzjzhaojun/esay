package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tenantpackage;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-01 20:08:23
 */

public interface TenantpackageMapper extends YtIBaseMapper<Tenantpackage> {
	/**
	 * add
	 * 
	 * @param o TSystenantpackage
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenantpackage.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSystenantpackagelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenantpackage.class })
	public Integer batchSava(List<Tenantpackage> list);

	/**
	 * update
	 * 
	 * @param o TSystenantpackage
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenantpackage.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSystenantpackage
	 */
	@YtRedisCacheAnnotation(classs = Tenantpackage.class)
	public Tenantpackage get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenantpackage.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tenantpackage.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSystenantpackage
	 */
	@YtRedisCacheAnnotation(classs = Tenantpackage.class)
	public List<Tenantpackage> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSystenantpackage
	 */
	@YtRedisCacheAnnotation(classs = Tenantpackage.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSystenantpackage
	 */
	@YtRedisCacheAnnotation(classs = Tenantpackage.class)
	public List<Tenantpackage> listByArrayId(long[] id);
}