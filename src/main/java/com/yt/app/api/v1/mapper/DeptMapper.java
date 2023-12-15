package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.Dept;
import com.yt.app.api.v1.vo.SysDeptTreeVO;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-10-27 14:55:02
 */

public interface DeptMapper extends YtIBaseMapper<Dept> {
	/**
	 * add
	 * 
	 * @param o TSysdept
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dept.class })
	public Integer post(Object t);

	/**
	 * sava batch
	 * 
	 * @param o TSysdeptlist
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dept.class })
	public Integer batchSava(List<Dept> list);

	/**
	 * update
	 * 
	 * @param o TSysdept
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dept.class })
	public Integer put(Object t);

	/**
	 * get
	 *
	 * @param id id
	 * @return TSysdept
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	public Dept get(Long id);

	/**
	 * delete
	 *
	 * @param id id
	 * @return count
	 */
	@YtRedisCacheEvictAnnotation(classs = { Dept.class })
	public Integer delete(Long id);

	/**
	 * listcount
	 * 
	 * @param param map
	 * @return count
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	public Integer countlist(Map<String, Object> param);

	/**
	 * list
	 * 
	 * @param param map
	 * @return listTSysdept
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	public List<Dept> list(Map<String, Object> param);

	/**
	 * map
	 * 
	 * @param param map
	 * @return mapTSysdept
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * listbyids
	 * 
	 * @param id long[]ids
	 * @return listTSysdept
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	public List<Dept> listByArrayId(long[] id);

	/**
	 * 列表
	 *
	 * @param filter 查询过滤参数
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/09 18:10
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	List<SysDeptTreeVO> selectDataList(Map<String, Object> param);

	/**
	 * 获取可用无限制的部门数据
	 *
	 * @return 查询结果
	 * @author zhengqingya
	 * @date 2023/10/13 11:17
	 */
	@YtRedisCacheAnnotation(classs = Dept.class)
	List<Dept> selectAllUsableList();
}