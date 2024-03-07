package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.vo.ExchangeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-07 20:55:20
 */

public interface ExchangeMapper extends YtIBaseMapper<Exchange> {
	/**
	 * add
	 * 
	 * @param o Exchange
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Exchange.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Exchangelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Exchange.class })
	public Integer batchSava(List<Exchange> list);

	/**
	 * update
	 * 
	 * @param o Exchange
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Exchange.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Exchange
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public Exchange get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Exchange.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listExchange
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public List<Exchange> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapExchange
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public List<ExchangeVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listExchange
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public List<Exchange> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payout
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public Exchange getByChannelOrdernum(String channelordernum);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payout
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public Exchange getByOrdernum(String ordernum);

	/**
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Exchange.class)
	public List<ExchangeVO> selectNotifylist();
}