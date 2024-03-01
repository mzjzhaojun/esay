package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Twitter;
import com.yt.app.api.v1.vo.TwitterVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-02-26 11:42:42
 */

public interface TwitterMapper extends YtIBaseMapper<Twitter> {
	/**
	 * add
	 * 
	 * @param o Twitter
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Twitter.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Twitterlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Twitter.class })
	public Integer batchSava(List<Twitter> list);

	/**
	 * update
	 * 
	 * @param o Twitter
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Twitter.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Twitter
	 */
	@YtRedisCacheAnnotation(classs = Twitter.class)
	public Twitter get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Twitter.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Twitter.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTwitter
	 */
	@YtRedisCacheAnnotation(classs = Twitter.class)
	public List<Twitter> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTwitter
	 */
	@YtRedisCacheAnnotation(classs = Twitter.class)
	public List<TwitterVO> pages(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTwitter
	 */
	@YtRedisCacheAnnotation(classs = Twitter.class)
	public List<Twitter> listByArrayId(long[] id);
}