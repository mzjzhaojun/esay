package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Agentaccountrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:44:01
 */

public interface AgentaccountrecordMapper extends YtIBaseMapper<Agentaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Agentaccountapplyjourna
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Agentaccountapplyjournalist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountrecord.class })
	public Integer batchSava(List<Agentaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Agentaccountapplyjourna
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agentaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountrecord.class)
	public Agentaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAgentaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountrecord.class)
	public List<Agentaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAgentaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAgentaccountapplyjourna
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountrecord.class)
	public List<Agentaccountrecord> listByArrayId(long[] id);
}