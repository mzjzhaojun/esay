package com.yt.app.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/11/27 14:38
 */
@Component
public class RedisHashUtil {

	@Autowired
	public RedisTemplate<String, Object> redisTemplate;

	/*********************** object *************************/

	public Object getObjectEx(final String mkey, final String key) {
		return redisTemplate.opsForHash().get(mkey, key);
	}

	public void setObjectEx(final String mkey, final String key, final Object value, final Integer timeout, final TimeUnit timeUnit) {
		redisTemplate.opsForHash().put(mkey, key, value);
	}

	public Boolean deleteObjectEx(final String mkey) {
		return redisTemplate.delete(mkey);
	}

}
