package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tenant;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-23 20:33:10
 */

public interface TenantMapper extends YtIBaseMapper<Tenant> {
	/**
	 * add
	 * 
	 * @param o TSystenant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenant.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSystenantlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenant.class })
	public Integer batchSava(List<Tenant> list);

	/**
	 * update
	 * 
	 * @param o TSystenant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenant.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSystenant
	 */
	@YtRedisCacheAnnotation(classs = Tenant.class)
	public Tenant get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tenant.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tenant.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSystenant
	 */
	@YtRedisCacheAnnotation(classs = Tenant.class)
	public List<Tenant> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSystenant
	 */
	@YtRedisCacheAnnotation(classs = Tenant.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSystenant
	 */
	@YtRedisCacheAnnotation(classs = Tenant.class)
	public List<Tenant> listByArrayId(long[] id);
	
	

	public List<Tenant> selectDataPage(Map<String, Object> param);

	/**
	 * 列表
	 *
	 * @param filter 查询过滤参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/08 15:40
	 */
	public List<Tenant> selectDataList(Map<String, Object> param);
}