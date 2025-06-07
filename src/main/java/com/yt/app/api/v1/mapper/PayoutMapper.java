package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-21 09:56:42
 */

public interface PayoutMapper extends YtIBaseMapper<Payout> {
	/**
	 * add
	 * 
	 * @param o Payout
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payout.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Payoutlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payout.class })
	public Integer batchSava(List<Payout> list);

	/**
	 * update
	 * 
	 * @param o Payout
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payout.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public Payout get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Payout.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listPayout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public List<Payout> list(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listPayout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public List<PayoutVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listPayout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public List<Payout> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public Payout getByMerchantOrdernum(String channelordernum);

	/**
	 * get
	 *
	 * @param id id
	 * @return Payout
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public Payout getByOrdernum(String ordernum);

	/**
	 * 代付通知
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public List<Payout> selectNotifylist();

	/**
	 * 
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Payout.class)
	public List<Payout> selectAddlist();

}