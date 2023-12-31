package com.yt.app.common.base.constant;

/**
 * <p>
 * 全局常用变量 - 线程池
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/5/27 10:52
 */
public interface ThreadPoolConstant {

	// ===============================================================================
	// ============================ ↓↓↓↓↓↓ 线程池 ↓↓↓↓↓↓ ==============================
	// ===============================================================================

	/**
	 */
	String SMALL_BOOT_THREAD_POOL = "PayBootThreadPoolTaskExecutor";

	// ===============================================================================
	// ============================ ↓↓↓↓↓↓ 线程池 - 线程名前缀 ↓↓↓↓↓↓ ===================
	// ===============================================================================

	/**
	 * 替换原生Spring默认线程池-线程名前缀
	 */
	String SPRING_DEFAULT_THREAD_NAME_PREFIX = "MyTaskExecutorInit-";
	/**
	 * PayBoot线程池-线程名前缀
	 */
	String SMALL_BOOT_THREAD_NAME_PREFIX = "PayBootTaskExecutor-";

}
