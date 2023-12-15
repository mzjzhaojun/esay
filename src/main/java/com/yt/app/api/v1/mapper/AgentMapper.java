package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-10 19:00:03
 */

public interface AgentMapper extends YtIBaseMapper<Agent> {
	/**
	 * add
	 * 
	 * @param o Agent
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agent.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Agentlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agent.class })
	public Integer batchSava(List<Agent> list);

	/**
	 * update
	 * 
	 * @param o Agent
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agent.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agent
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public Agent get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agent.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAgent
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public List<Agent> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAgent
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAgent
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public List<Agent> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agent
	 */
	@YtRedisCacheAnnotation(classs = Agent.class)
	public Agent getByUserId(Long userid);

}