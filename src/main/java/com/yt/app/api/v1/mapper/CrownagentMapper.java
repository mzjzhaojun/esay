package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Crownagent;
import com.yt.app.api.v1.vo.CrownagentVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:06
 */

public interface CrownagentMapper extends YtIBaseMapper<Crownagent> {
	/**
	 * add
	 * 
	 * @param o Crownagent
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Crownagent.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Crownagentlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Crownagent.class })
	public Integer batchSava(List<Crownagent> list);

	/**
	 * update
	 * 
	 * @param o Crownagent
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Crownagent.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Crownagent
	 */
	@YtRedisCacheAnnotation(classs = Crownagent.class)
	public Crownagent get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Crownagent.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Crownagent.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listCrownagent
	 */
	@YtRedisCacheAnnotation(classs = Crownagent.class)
	public List<Crownagent> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapCrownagent
	 */
	@YtRedisCacheAnnotation(classs = Crownagent.class)
	public List<CrownagentVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listCrownagent
	 */
	@YtRedisCacheAnnotation(classs = Crownagent.class)
	public List<Crownagent> listByArrayId(long[] id);
}