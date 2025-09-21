package com.yt.app.api.v1.mapper;

import java.util.List;
import java.util.Map;

import com.yt.app.api.v1.entity.YtFile;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.base.YtIBaseMapper;

/**
 * @Ytauthor zj default
 * 
 * @Ytversion 1.1
 */

public interface FileMapper extends YtIBaseMapper<YtFile> {
	/**
	 * 保存（持久化）对象
	 * 
	 * @Ytparam o 要持久化的对象
	 * @Ytreturn 执行成功的记录个数
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public Integer post(Object t);

	/**
	 * 更新（持久化）对象
	 * 
	 * @Ytparam o 要持久化的对象
	 * @Ytreturn 执行成功的记录个数
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public Integer put(Object t);

	/**
	 * 获取指定的唯一标识符对应的持久化对象
	 *
	 * @Ytparam id 指定的唯一标识符
	 * @Ytreturn 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public YtFile get(Long id);

	/**
	 * 删除指定的唯一标识符数组对应的持久化对象
	 *
	 * @Ytparam ids 指定的唯一标识符数组
	 * @Ytreturn 删除的对象数量
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public Integer delById(Long id);

	/**
	 * 获取满足查询参数条件的数据总数
	 * 
	 * @Ytparam param 查询参数
	 * @Ytreturn 数据总数
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public Integer countlist(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据总数
	 * 
	 * @Ytparam param 查询参数
	 * @Ytreturn 数据总数
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public Integer countmap(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @Ytparam param 查询参数
	 * @Ytreturn 数据
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public List<YtFile> list(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @Ytparam param 查询参数
	 * @Ytreturn 数据
	 */
	@YtRedisCacheEvictAnnotation(classs = { YtFile.class })
	public List<Map<String, Object>> map(Map<String, Object> param);
}