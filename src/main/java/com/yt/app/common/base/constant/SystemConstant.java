package com.yt.app.common.base.constant;

/**
 * <p>
 * 全局常用变量 - system
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/6/15 14:38
 */
public interface SystemConstant {

	// ===============================================================================
	// ============================ ↓↓↓↓↓↓ redis缓存系列 ↓↓↓↓↓↓
	// ============================
	// ===============================================================================

	/**
	 * 数据字典缓存
	 */
	String CACHE_SYS_DICT_PREFIX = "payboot:system:dict:";

	/**
	 * 数据字典类型缓存
	 */
	String CACHE_SYS_DICTTYPE_PREFIX = "payboot:system:dicttype:";
	/**
	 * 系统配置缓存
	 */
	String CACHE_SYS_CONFIG_PREFIX = "payboot:system:config:";
	/**
	 * 系统缓存
	 */
	String CACHE_SYS_MENU_TREE = "payboot:system:sys_menu_tree";
	/**
	 * 个人缓存
	 */
	String CACHE_SYS_USER_INFO_PREFIX = "payboot:system:sys_user_info_";
	/**
	 * 权限缓存
	 */
	String CACHE_SYS_PERMISSION_PREFIX = "payboot:system:sys_permission_";
	/**
	 * 实时汇率
	 */
	String CACHE_SYS_EXCHANGE = "payboot:system:exchange:";

	/**
	 * 挂碼
	 */
	String CACHE_SYS_QRCODE = "payboot:system:qrcode:";

	/**
	 * 兑换trx
	 */
	String CACHE_SYS_EXCHANGETRX = "payboot:system:exchangetrx:";

	/**
	 * 存在订单
	 */
	String CACHE_SYS_PAYOUT_EXIST = "payboot:system:payoutexist:";
}
