package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.QrcodeaisleqrcodeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-22 14:27:18
 */

public interface QrcodeaisleqrcodeMapper extends YtIBaseMapper<Qrcodeaisleqrcode> {
	/**
	 * add
	 * 
	 * @param o Qrcodeaisleqrcode
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodeaisleqrcodelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer batchSava(List<Qrcodeaisleqrcode> list);

	/**
	 * update
	 * 
	 * @param o Qrcodeaisleqrcode
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodeaisleqrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public Qrcodeaisleqrcode get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodeaisleqrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public List<Qrcodeaisleqrcode> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodeaisleqrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public List<QrcodeaisleqrcodeVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaisleqrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public List<Qrcodeaisleqrcode> listByArrayId(long[] id);

	/**
	 * getByQrcodeAisleId
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaisleqrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public List<Qrcodeaisleqrcode> getByQrcodeAisleId(long qrcodeaisleid);

	/**
	 * 查询重复
	 * 
	 * @param qrcodelid
	 * @param qrcodeaisleid
	 * @return
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaisleqrcode.class)
	public Qrcodeaisleqrcode getByAidCid(@Param("qrcodelid") Long qrcodelid,
			@Param("qrcodeaisleid") Long qrcodeaisleid);

	/**
	 * deleteByQrcodeaisleId
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer deleteByQrcodeaisleId(Long id);

	/**
	 * deleteByQrcodelId
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaisleqrcode.class })
	public Integer deleteByQrcodelId(Long id);

}