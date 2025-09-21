package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.vo.TronmemberVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-08 01:31:33
 */

public interface TronmemberMapper extends YtIBaseMapper<Tronmember> {
	/**
	 * add
	 * 
	 * @param o Tronmember
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmember.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tronmemberlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmember.class })
	public Integer batchSava(List<Tronmember> list);

	/**
	 * update
	 * 
	 * @param o Tronmember
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmember.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tronmember
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public Tronmember get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmember.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTronmember
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public List<Tronmember> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTronmember
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public List<TronmemberVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTronmember
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public List<Tronmember> listByArrayId(long[] id);

	/**
	 * getByTgId
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tronmember.class)
	public Tronmember getByTgId(Long id);

}