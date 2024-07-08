package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Merchantaccountrecord;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

public interface MerchantaccountrecordMapper extends YtIBaseMapper<Merchantaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountrecord.class })
	public Integer post(Object t);

	/**
	 * add
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountrecord.class })
	public Integer postAndTenantid(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Merchantaccountapplyjournallist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountrecord.class })
	public Integer batchSava(List<Merchantaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Merchantaccountapplyjournal
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Merchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountrecord.class)
	public Merchantaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Merchantaccountrecord.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountrecord.class)
	public List<Merchantaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountrecord.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listMerchantaccountapplyjournal
	 */
	@YtRedisCacheAnnotation(classs = Merchantaccountrecord.class)
	public List<Merchantaccountrecord> listByArrayId(long[] id);
}