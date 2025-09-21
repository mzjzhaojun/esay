package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcodpaymember;
import com.yt.app.api.v1.vo.QrcodpaymemberVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-16 23:44:12
 */

public interface QrcodpaymemberMapper extends YtIBaseMapper<Qrcodpaymember> {
	/**
	 * add
	 * 
	 * @param o Qrcodpaymember
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodpaymember.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodpaymemberlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodpaymember.class })
	public Integer batchSava(List<Qrcodpaymember> list);

	/**
	 * update
	 * 
	 * @param o Qrcodpaymember
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodpaymember.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodpaymember
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public Qrcodpaymember get(Long id);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodpaymember
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public Qrcodpaymember getByMermberId(String id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodpaymember.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodpaymember
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public List<Qrcodpaymember> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodpaymember
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public List<QrcodpaymemberVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodpaymember
	 */
	@YtRedisCacheAnnotation(classs = Qrcodpaymember.class)
	public List<Qrcodpaymember> listByArrayId(long[] id);
}