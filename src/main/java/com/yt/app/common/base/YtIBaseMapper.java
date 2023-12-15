/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yt.app.common.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * BaseMapper
 *
 * @author zj
 * @since 2016
 */
public interface YtIBaseMapper<T> extends BaseMapper<T> {

	/**
	 * 保存（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer post(Object t);

	/**
	 * 批量保存
	 * 
	 * 
	 * @param list
	 * @return
	 */
	public Integer trainRecordBatch(List<T> list);

	/**
	 * 更新（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public Integer put(Object t);

	/**
	 * 获取指定的唯一标识符对应的持久化对象
	 *
	 * @param id 指定的唯一标识符
	 * @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
	 */
	public T get(Long id);

	/**
	 * 删除指定的唯一标识符数组对应的持久化对象
	 *
	 * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
	 */
	public Integer delete(Long id);

	/**
	 * 获取满足查询参数条件的数据总数
	 * 
	 * @param param 查询参数
	 * @return 数据总数
	 */
	public Integer countlist(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @param param 查询参数
	 * @return 数据
	 */
	public List<T> list(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @param param 查询参数
	 * @return 数据
	 */
	public List<Map<String, Object>> map(Map<String, Object> param);

	/**
	 * 获取满足查询参数条件的数据
	 * 
	 * @param id
	 * @return
	 */
	public List<T> listByArrayId(long[] id);

	/**
	 * 批量保存 -- MySQL
	 *
	 * @param entityList 保存实体类
	 * @return 操作数
	 * @author zhengqingya
	 * @date 2020/8/3 18:41
	 */
	int insertBatchSomeColumn(List<T> entityList);

	/**
	 * 执行sql
	 *
	 * @param sql sql
	 * @return void
	 * @author zhengqingya
	 * @date 2022/7/22 16:46
	 */
	@Update("#{sql}")
	void execSql(@Param("sql") String sql);

}
