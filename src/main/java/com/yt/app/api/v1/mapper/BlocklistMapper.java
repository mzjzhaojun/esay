package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Blocklist;
import com.yt.app.api.v1.vo.BlocklistVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-03-19 14:56:50
 */

public interface BlocklistMapper extends YtIBaseMapper<Blocklist> {
	/**
	 * add
	 * 
	 * @param o Blocklist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Blocklist.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Blocklistlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Blocklist.class })
	public Integer batchSava(List<Blocklist> list);

	/**
	 * update
	 * 
	 * @param o Blocklist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Blocklist.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Blocklist
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public Blocklist get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Blocklist.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listBlocklist
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public List<Blocklist> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapBlocklist
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public List<BlocklistVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listBlocklist
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public List<Blocklist> listByArrayId(long[] id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Blocklist
	 */
	@YtRedisCacheAnnotation(classs = Blocklist.class)
	public Blocklist getByHexaddress(String hexaddress);

}