package com.yt.app.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.enums.YtDataSourceEnum;

/**
 * 
 * Baseservice
 * 
 * @author zj
 * 
 */
public interface YtIBaseService<T, ID extends Serializable> {

	/**
	 * 保存（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer post(T t);

	/**
	 * 更新（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer put(T t);

	/**
	 * 获取指定的唯一标识符对应的持久化对象
	 *
	 * @param id 指定的唯一标识符
	 * @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public T get(Long id);

	/**
	 * 删除指定的唯一标识符数组对应的持久化对象
	 *
	 * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.MASTER)
	public Integer delete(Long id);

	/**
	 * 获取满足查询参数条件的List数据，如果存在分页参数则返回分页数据
	 * 
	 * @param param
	 * @return
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<T> list(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的List数据
	 * 
	 * @param param
	 * @return
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<T> list();

	/**
	 * 获取满足查询参数条件的Map数据，如果存在分页参数则返回分页数据
	 * 
	 * @param param
	 * @return
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<List<Map<String, Object>>> map(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的List数据
	 * 
	 * @return
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<Map<String, Object>> map();

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @param id
	 * @return
	 */
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public List<T> listByArrayId(long[] id);

}
