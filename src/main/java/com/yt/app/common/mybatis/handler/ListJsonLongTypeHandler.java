package com.yt.app.common.mybatis.handler;

import com.yt.app.common.mybatis.base.DbJsonTypeHandler;

/**
 * <p>
 * 自定义类型处理器
 * </p>
 *
 * @author zhengqingya
 * @description eg: @TableField(typeHandler = ListJsonIntegerTypeHandler.class)
 *              数据库json数据 [1,2,3] =》 java的 List<Integer>
 * @date 2022/6/6 11:26
 */
public class ListJsonLongTypeHandler extends DbJsonTypeHandler {

	public ListJsonLongTypeHandler(Class<?> type) {
		super(type, Long.class);
	}

}