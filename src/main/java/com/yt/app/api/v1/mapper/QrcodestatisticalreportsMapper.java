package com.yt.app.api.v1.mapper;
import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Qrcodestatisticalreports;
import com.yt.app.api.v1.vo.QrcodestatisticalreportsVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;


/**
* @author zj default
* 
* @version v1
* @createdate2025-03-19 19:51:13
*/

public interface QrcodestatisticalreportsMapper extends YtIBaseMapper<Qrcodestatisticalreports> {
/**
* add
* 
* @param o
* Qrcodestatisticalreports
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Qrcodestatisticalreports.class})
public Integer post(Object t);
/**
* sava batch
* 
* @param o
*  Qrcodestatisticalreportslist
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Qrcodestatisticalreports.class})
public Integer batchSava(List<Qrcodestatisticalreports> list);
/**
* update
* 
* @param o
* Qrcodestatisticalreports
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Qrcodestatisticalreports.class})
public Integer put(Object t);

/**
* get
*
* @param id
* id
* @return Qrcodestatisticalreports
*/
@YtRedisCacheAnnotation(classs = Qrcodestatisticalreports.class)
public Qrcodestatisticalreports get(Long id);

/**
* delete
*
* @param id
* id
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Qrcodestatisticalreports.class})
public Integer delete(Long id);

/**
* listcount
* 
* @param param
* map
* @return count
*/
@YtRedisCacheAnnotation(classs = Qrcodestatisticalreports.class)
public Integer countlist(Map<String, Object> param);

/**
* list
* 
* @param param
* map
* @return listQrcodestatisticalreports
*/
@YtRedisCacheAnnotation(classs = Qrcodestatisticalreports.class)
public List<Qrcodestatisticalreports> list(Map<String, Object> param);

/**
* map
* 
* @param param
* map
* @return mapQrcodestatisticalreports
*/
@YtRedisCacheAnnotation(classs = Qrcodestatisticalreports.class)
public List<QrcodestatisticalreportsVO> page(Map<String, Object> param);

/**
* listbyids
* 
* @param id
* long[]ids
* @return listQrcodestatisticalreports
*/
@YtRedisCacheAnnotation(classs = Qrcodestatisticalreports.class)
public List<Qrcodestatisticalreports> listByArrayId(long[] id);
}