package com.yt.app.common.base;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;

import com.yt.app.common.common.yt.YtRequestEntity;
import com.yt.app.common.common.yt.YtResponseEntity;

/**
 * 
 * BaseController
 * 
 * @author zj
 * 
 */
public interface YtIBaseController<T, ID extends Serializable> {

	/**
	 * 获取当前登录人ID
	 *
	 * @return 当前登录人ID
	 * @author zhengqingya
	 * @date 2020/8/30 15:41
	 */
	public Long appGetCurrentUserId();

	/**
	 * 获取当前登录人关联的部门ID
	 *
	 * @return 部门ID
	 * @author zhengqingya
	 * @date 2020/8/30 15:41
	 */
	public Long getCurrentUserReDeptId();

	/**
	 * 保存（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public YtResponseEntity<Object> post(YtRequestEntity<T> requestEntity, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 更新（持久化）对象
	 * 
	 * @param o 要持久化的对象
	 * @return 执行成功的记录个数
	 */
	public YtResponseEntity<Object> put(YtRequestEntity<T> requestEntity, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 获取指定的唯一标识符对应的持久化对象
	 * 
	 * @param id 指定的唯一标识符
	 * @return 指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
	 */
	public YtResponseEntity<Object> get(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 删除指定的唯一标识符数组对应的持久化对象
	 * 
	 * @param ids 指定的唯一标识符数组
	 * @return 删除的对象数量
	 */
	public YtResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * 全部数据或有分页参数返回分页数据
	 * 
	 * @return 查询结果列表
	 */
	public YtResponseEntity<Object> list(YtRequestEntity<Object> requestEntity, HttpServletRequest request,
			HttpServletResponse response);

}
