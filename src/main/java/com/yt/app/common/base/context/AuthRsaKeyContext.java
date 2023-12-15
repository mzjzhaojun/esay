package com.yt.app.common.base.context;

/**
 * <p>
 * rsakey
 * </p>
 *
 * @author zhengqingya
 * @description {@link com.zhengqing.common.base.enums.AuthSourceEnum}
 * @date 2021/6/30 9:24 下午
 */
public class AuthRsaKeyContext {

	public static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

	public static final ThreadLocal<String> THREAD_AES_LOCAL = new ThreadLocal<>();

	public static void setKey(String key) {
		THREAD_LOCAL.set(key);
	}

	public static String getKey() {
		return THREAD_LOCAL.get();
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}

	public static void setAesKey(String key) {
		THREAD_AES_LOCAL.set(key);
	}

	public static String getAesKey() {
		return THREAD_AES_LOCAL.get();
	}

	public static void removeAes() {
		THREAD_AES_LOCAL.remove();
	}

}
