package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Betting;
import com.yt.app.api.v1.vo.BettingVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-08-12 22:27:16
 */

public interface BettingMapper extends YtIBaseMapper<Betting> {
	/**
	 * add
	 * 
	 * @param o Betting
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Betting.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Bettinglist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Betting.class })
	public Integer batchSava(List<Betting> list);

	/**
	 * update
	 * 
	 * @param o Betting
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Betting.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Betting
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public Betting get(Long id);

	/**
	 * getbyTid
	 *
	 * @param id id
	 * @return Betting
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public Betting getByTid(String tid);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Betting.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listBetting
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public List<Betting> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapBetting
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public List<BettingVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listBetting
	 */
	@YtRedisCacheAnnotation(classs = Betting.class)
	public List<Betting> listByArrayId(long[] id);
}