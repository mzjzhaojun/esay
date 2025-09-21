package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcodetransferrecord;
import com.yt.app.api.v1.vo.QrcodetransferrecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2025-04-13 22:38:13
 */

public interface QrcodetransferrecordMapper extends YtIBaseMapper<Qrcodetransferrecord> {
	/**
	 * add
	 * 
	 * @param o Qrcodetransferrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodetransferrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodetransferrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodetransferrecord.class })
	public Integer batchSava(List<Qrcodetransferrecord> list);

	/**
	 * update
	 * 
	 * @param o Qrcodetransferrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodetransferrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodetransferrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodetransferrecord.class)
	public Qrcodetransferrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodetransferrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodetransferrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodetransferrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodetransferrecord.class)
	public List<Qrcodetransferrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodetransferrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodetransferrecord.class)
	public List<QrcodetransferrecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodetransferrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodetransferrecord.class)
	public List<Qrcodetransferrecord> listByArrayId(long[] id);
}