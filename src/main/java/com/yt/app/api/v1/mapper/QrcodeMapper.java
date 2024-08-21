package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.vo.QrcodeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */

public interface QrcodeMapper extends YtIBaseMapper<Qrcode> {
	/**
	 * add
	 * 
	 * @param o Qrcode
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcode.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodelist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcode.class })
	public Integer batchSava(List<Qrcode> list);

	/**
	 * update
	 * 
	 * @param o Qrcode
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcode.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcode.class)
	public Qrcode get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcode.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcode.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcode.class)
	public List<Qrcode> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcode.class)
	public List<QrcodeVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcode
	 */
	@YtRedisCacheAnnotation(classs = Qrcode.class)
	public List<Qrcode> listByArrayId(long[] id);
}