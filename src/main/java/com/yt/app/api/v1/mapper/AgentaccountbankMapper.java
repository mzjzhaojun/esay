package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Agentaccountbank;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-16 10:39:42
 */

public interface AgentaccountbankMapper extends YtIBaseMapper<Agentaccountbank> {
	/**
	 * add
	 * 
	 * @param o Agentaccountbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountbank.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Agentaccountbanklist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountbank.class })
	public Integer batchSava(List<Agentaccountbank> list);

	/**
	 * update
	 * 
	 * @param o Agentaccountbank
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountbank.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Agentaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public Agentaccountbank get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Agentaccountbank.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listAgentaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public List<Agentaccountbank> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapAgentaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listAgentaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public List<Agentaccountbank> listByArrayId(long[] id);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountbank
	 */
	@YtRedisCacheAnnotation(classs = Agentaccountbank.class)
	public List<Agentaccountbank> listByUserid(Long userid);
}