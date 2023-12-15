package com.yt.app.common.base.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * <p>
 * 全局常用变量 - base
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/10/12 14:47
 */
public interface BaseConstant {

	/**
	 * 第一级父类id
	 */
	Long PARENT_ID = 0L;

	/**
	 * 超级管理员 -- 用户id、角色id
	 */
	Long SYSTEM_SUPER_ADMIN_USER_ID = 1L;
	Long SYSTEM_SUPER_ADMIN_ROLE_ID = 1L;

	// 系统管理员 -- 用户id、角色id
	Long SYSTEM_ADMIN_USER_ID = 2L;
	Long SYSTEM_ADMIN_ROLE_ID = 2L;

	// 租户套餐 变更时 不清除的角色关联菜单数据
	List<Long> NOT_DEL_MENU_EXCLUDE_ROLE_ID_LIST = Lists.newArrayList(1L, 2L);

	/**
	 * 项目租户 -- id、超级套餐
	 */
	Long SYSTEM_TENANT_ID = 1L;
	Long SYSTEM_TENANT_ID_PACKAGE_ID = 1L;

	/**
	 * 获取项目根目录
	 */
	String PROJECT_ROOT_DIRECTORY = System.getProperty("user.dir").replaceAll("\\\\", "/");

	/**
	 * 用户ID、用户名、消息上下文Key 【注：key名称一定不要和前端请求参数中的属性名一样，否则会拿不到真正的值！！！】
	 */
	String CONTEXT_KEY_SYS_USER_ID = "payboot_sys_user_id";
	String CONTEXT_KEY_USERNAME = "payboot_username";
	String DEFAULT_CONTEXT_KEY_USER_ID = "0";
	String DEFAULT_CONTEXT_KEY_USERNAME = "未知";
	String DEFAULT_CONTEXT_KEY_PASSWORD = "123456";
}
