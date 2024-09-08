package com.yt.app.api.v1.mapper;
import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tronrecord;
import com.yt.app.api.v1.vo.TronrecordVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;


/**
* @author zj default
* 
* @version v1
* @createdate2024-09-08 01:31:33
*/

public interface TronrecordMapper extends YtIBaseMapper<Tronrecord> {
/**
* add
* 
* @param o
* Tronrecord
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tronrecord.class})
public Integer post(Object t);
/**
* sava batch
* 
* @param o
*  Tronrecordlist
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tronrecord.class})
public Integer batchSava(List<Tronrecord> list);
/**
* update
* 
* @param o
* Tronrecord
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tronrecord.class})
public Integer put(Object t);

/**
* get
*
* @param id
* id
* @return Tronrecord
*/
@YtRedisCacheAnnotation(classs = Tronrecord.class)
public Tronrecord get(Long id);

/**
* delete
*
* @param id
* id
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tronrecord.class})
public Integer delete(Long id);

/**
* listcount
* 
* @param param
* map
* @return count
*/
@YtRedisCacheAnnotation(classs = Tronrecord.class)
public Integer countlist(Map<String, Object> param);

/**
* list
* 
* @param param
* map
* @return listTronrecord
*/
@YtRedisCacheAnnotation(classs = Tronrecord.class)
public List<Tronrecord> list(Map<String, Object> param);

/**
* map
* 
* @param param
* map
* @return mapTronrecord
*/
@YtRedisCacheAnnotation(classs = Tronrecord.class)
public List<TronrecordVO> page(Map<String, Object> param);

/**
* listbyids
* 
* @param id
* long[]ids
* @return listTronrecord
*/
@YtRedisCacheAnnotation(classs = Tronrecord.class)
public List<Tronrecord> listByArrayId(long[] id);
}