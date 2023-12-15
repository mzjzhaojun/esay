package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface AgentaccountMapper extends YtIBaseMapper<Agentaccount> {
	/**
	 * add
	 * 
	 * @param o Agentaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccount.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Agentaccountlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccount.class })
	public Integer batchSava(List<Agentaccount> list);

	/**
	 * update
	 * 
	 * @param o Agentaccount
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccount.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agentaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public Agentaccount get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccount.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAgentaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public List<Agentaccount> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAgentaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAgentaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public List<Agentaccount> listByArrayId(long[] id);

	/**
	 * getByUserId
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccount.class)
	public Agentaccount getByUserId(Long userid);
}