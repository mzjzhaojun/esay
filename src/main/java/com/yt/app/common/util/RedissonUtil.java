package com.yt.app.common.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Redis工具类
 * </p>
 *
 * @description
 * @date 2019/11/27 14:38
 */
@Component
public class RedissonUtil {

	private static RedissonClient redissonclient;

	@Autowired
	public RedissonUtil(RedissonClient redissonclient) {
		RedissonUtil.redissonclient = redissonclient;
	}

	public static RLock getLock(Long key) {
		RLock lock = redissonclient.getLock(key.toString());
		return lock;
	}

}
