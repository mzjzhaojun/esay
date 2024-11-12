package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.api.v1.vo.TgmessagegroupVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-05 13:07:39
 */

public interface TgmessagegroupMapper extends YtIBaseMapper<Tgmessagegroup> {
	/**
	 * add
	 * 
	 * @param o Tgmessagegroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmessagegrouplist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer batchSava(List<Tgmessagegroup> list);

	/**
	 * update
	 * 
	 * @param o Tgmessagegroup
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public Tgmessagegroup get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public List<Tgmessagegroup> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public List<TgmessagegroupVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public List<Tgmessagegroup> listByArrayId(long[] id);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public Tgmessagegroup getByTgGroupName(String groupname);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public Tgmessagegroup getByTgGroupId(Long tgid);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return Tgmessagegroup
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegroup.class)
	public Tgmessagegroup getByMerchantId(Long Id);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer updatetodayvalue(Long Id);

	/**
	 * update
	 * 
	 * @param o Merchant
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegroup.class })
	public Integer updatemerchantid(Long Id);

}