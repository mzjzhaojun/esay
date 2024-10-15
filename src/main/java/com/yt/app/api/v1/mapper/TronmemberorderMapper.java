package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.vo.TronmemberorderVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-10-15 00:23:49
 */

public interface TronmemberorderMapper extends YtIBaseMapper<Tronmemberorder> {
	/**
	 * add
	 * 
	 * @param o Tronmemberorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmemberorder.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Tronmemberorderlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmemberorder.class })
	public Integer batchSava(List<Tronmemberorder> list);

	/**
	 * update
	 * 
	 * @param o Tronmemberorder
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmemberorder.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Tronmemberorder
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public Tronmemberorder get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Tronmemberorder.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTronmemberorder
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public List<Tronmemberorder> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTronmemberorder
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public List<TronmemberorderVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTronmemberorder
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public List<Tronmemberorder> listByArrayId(long[] id);

	/**
	 * getByTxId
	 *
	 * @param id txid
	 * @return Tronmemberorder
	 */
	@YtRedisCacheAnnotation(classs = Tronmemberorder.class)
	public Tronmemberorder getByTxId(String txid);

}