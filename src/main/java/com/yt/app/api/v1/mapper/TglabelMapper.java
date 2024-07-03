package com.yt.app.api.v1.mapper;
import java.util.List;
import java.util.Map;
import com.yt.app.api.v1.entity.Tglabel;
import com.yt.app.api.v1.vo.TglabelVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;


/**
* @author zj default
* 
* @version v1
* @createdate2024-07-02 20:41:40
*/

public interface TglabelMapper extends YtIBaseMapper<Tglabel> {
/**
* add
* 
* @param o
* Tglabel
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tglabel.class})
public Integer post(Object t);
/**
* sava batch
* 
* @param o
*  Tglabellist
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tglabel.class})
public Integer batchSava(List<Tglabel> list);
/**
* update
* 
* @param o
* Tglabel
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tglabel.class})
public Integer put(Object t);

/**
* get
*
* @param id
* id
* @return Tglabel
*/
@YtRedisCacheAnnotation(classs = Tglabel.class)
public Tglabel get(Long id);

/**
* delete
*
* @param id
* id
* @return count
*/
@YtRedisCacheEvictAnnotation(classs = { Tglabel.class})
public Integer delete(Long id);

/**
* listcount
* 
* @param param
* map
* @return count
*/
@YtRedisCacheAnnotation(classs = Tglabel.class)
public Integer countlist(Map<String, Object> param);

/**
* list
* 
* @param param
* map
* @return listTglabel
*/
@YtRedisCacheAnnotation(classs = Tglabel.class)
public List<Tglabel> list(Map<String, Object> param);

/**
* map
* 
* @param param
* map
* @return mapTglabel
*/
@YtRedisCacheAnnotation(classs = Tglabel.class)
public List<TglabelVO> page(Map<String, Object> param);

/**
* listbyids
* 
* @param id
* long[]ids
* @return listTglabel
*/
@YtRedisCacheAnnotation(classs = Tglabel.class)
public List<Tglabel> listByArrayId(long[] id);
}