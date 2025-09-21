package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcodeaccountrecord;
import com.yt.app.api.v1.vo.QrcodeaccountrecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 22:50:47
 */

public interface QrcodeaccountrecordMapper extends YtIBaseMapper<Qrcodeaccountrecord> {
	/**
	 * add
	 * 
	 * @param o Qrcodeaccountrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountrecord.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o Qrcodeaccountrecordlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountrecord.class })
	public Integer batchSava(List<Qrcodeaccountrecord> list);

	/**
	 * update
	 * 
	 * @param o Qrcodeaccountrecord
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountrecord.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return Qrcodeaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountrecord.class)
	public Qrcodeaccountrecord get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Qrcodeaccountrecord.class })
	public Integer delById(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountrecord.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listQrcodeaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountrecord.class)
	public List<Qrcodeaccountrecord> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapQrcodeaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountrecord.class)
	public List<QrcodeaccountrecordVO> page(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listQrcodeaccountrecord
	 */
	@YtRedisCacheAnnotation(classs = Qrcodeaccountrecord.class)
	public List<Qrcodeaccountrecord> listByArrayId(long[] id);
}