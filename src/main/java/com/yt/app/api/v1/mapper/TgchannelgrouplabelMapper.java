package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tgchannelgrouplabel;
import com.yt.app.api.v1.vo.TgchannelgrouplabelVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-07-02 20:41:40
 */

public interface TgchannelgrouplabelMapper extends YtIBaseMapper<Tgchannelgrouplabel> {
	/**
	 * add
	 * 
	 * @param o Tglabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgrouplabel.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tglabellist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgrouplabel.class })
	public Integer batchSava(List<Tgchannelgrouplabel> list);

	/**
	 * update
	 * 
	 * @param o Tglabel
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgrouplabel.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tglabel
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgrouplabel.class)
	public Tgchannelgrouplabel get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tgchannelgrouplabel.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgrouplabel.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTglabel
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgrouplabel.class)
	public List<Tgchannelgrouplabel> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTglabel
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgrouplabel.class)
	public List<TgchannelgrouplabelVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTglabel
	 */
	@YtRedisCacheAnnotation(classs = Tgchannelgrouplabel.class)
	public List<Tgchannelgrouplabel> listByArrayId(long[] id);
}