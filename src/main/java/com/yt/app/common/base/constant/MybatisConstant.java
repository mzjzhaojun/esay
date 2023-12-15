package com.yt.app.common.base.constant;

/**
 * <p>
 * 全局常用变量 - mybatis
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/1 17:08
 */
public interface MybatisConstant {

	/**
	 * mybatis 分页参数
	 */
	String LIMIT_ONE = "LIMIT 1";
	/**
	 * 是否删除：true->删除，false->未删除
	 */
	String IS_DELETED = "is_deleted";
	/**
	 * 创建人id
	 */
	String CREATE_BY = "create_by";
	/**
	 * 创建时间
	 */
	String CREATE_TIME = "create_time";
	/**
	 * 更新人id
	 */
	String UPDATE_BY = "update_by";
	/**
	 * 更新时间
	 */
	String UPDATE_TIME = "update_time";

}
