package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Tgmessagegrouprecord;
import com.yt.app.api.v1.vo.TgmessagegrouprecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-09-19 01:40:22
 */

public interface TgmessagegrouprecordMapper extends YtIBaseMapper<Tgmessagegrouprecord> {
	/**
	 * add
	 * 
	 * @param o Tgmessagegrouprecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegrouprecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tgmessagegrouprecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegrouprecord.class })
	public Integer batchSava(List<Tgmessagegrouprecord> list);

	/**
	 * update
	 * 
	 * @param o Tgmessagegrouprecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegrouprecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public Tgmessagegrouprecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgmessagegrouprecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public List<Tgmessagegrouprecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public List<TgmessagegrouprecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public List<Tgmessagegrouprecord> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public Tgmessagegrouprecord getCidReplyid(@Param("cid") String cid, @Param("creplyid") Integer creplyid);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tgmessagegrouprecord
	 */
	@YtRedisCacheAnnotation(classs = Tgmessagegrouprecord.class)
	public Tgmessagegrouprecord getMidReplyid(@Param("mid") String mid, @Param("mreplyid") Integer mreplyid);
}