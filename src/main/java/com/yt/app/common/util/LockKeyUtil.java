package com.yt.app.common.util;

import java.util.Hashtable;
import java.util.Map;

public class LockKeyUtil {
	private static Map<String, String> lockMap = new Hashtable<>();

	@SuppressWarnings("unlikely-arg-type")
	public static String getLockKey(String lockKey) {
		if (lockMap.get(lockMap) != null) {
			return lockMap.get(lockKey);
		}
		synchronized (LockKeyUtil.class) {
			if (lockMap.get(lockMap) != null) {
				return lockMap.get(lockKey);
			}
			lockMap.put(lockKey, lockKey);
			return lockKey;
		}
	}
}
