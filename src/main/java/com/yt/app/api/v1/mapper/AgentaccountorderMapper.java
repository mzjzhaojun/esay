package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-18 12:41:23
 */

public interface AgentaccountorderMapper extends YtIBaseMapper<Agentaccountorder> {
	/**
	 * add
	 * 
	 * @param o Agentaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountorder.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Agentaccountorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountorder.class })
	public Integer batchSava(List<Agentaccountorder> list);

	/**
	 * update
	 * 
	 * @param o Agentaccountorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agentaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public Agentaccountorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountorder.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAgentaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public List<Agentaccountorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAgentaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAgentaccountorder
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public List<Agentaccountorder> listByArrayId(long[] id);

	/**
	 * getByOrdernum
	 *
	 * @param id id
	 * @return Merchantaccount
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountorder.class)
	public Agentaccountorder getByOrdernum(String ordernum);
}