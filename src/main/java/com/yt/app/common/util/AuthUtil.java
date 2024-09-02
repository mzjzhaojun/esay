package com.yt.app.common.util;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.bo.JwtUserBO;
import com.yt.app.api.v1.vo.AuthLoginVO;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 授权工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/4/15 11:33
 */
@Component
public class AuthUtil {

	private final static String JWT_USER_KEY = "payboot:login:";

	private static String tokenPrefix;

	private static long timeout;

	@Autowired
	RedisUtil rs;

	@Autowired
	public AuthUtil(@Value("${sa-token.token-prefix:}") String tokenPrefix,
			@Value("${sa-token.timeout:}") long timeout) {
		AuthUtil.tokenPrefix = tokenPrefix;
		AuthUtil.timeout = timeout;
	}

	/**
	 * 登录认证
	 *
	 * @param jwtUserBO 用户token信息
	 * @return 登录参数
	 * @author zhengqingya
	 * @date 2020/4/15 11:33
	 */
	public static AuthLoginVO login(JwtUserBO jwtUserBO) {
		Long userId = jwtUserBO.getUserId();
		Assert.notNull(userId, "用户id不能为空！");

		StpUtil.login(userId);

		String tokenValue = StpUtil.getTokenValue();
		// 将登录信息存储到redis
		RedisUtil.setEx(JWT_USER_KEY + tokenValue, JSONUtil.toJsonStr(jwtUserBO), timeout, TimeUnit.SECONDS);

		return AuthLoginVO.builder().tokenName(StpUtil.getTokenName())
				.tokenValue(StrUtil.isNotBlank(tokenPrefix) ? tokenPrefix + " " + tokenValue : tokenValue)
				.tenantId(jwtUserBO.getTenantId()).build();
	}

	/**
	 * 根据用户id注销
	 *
	 * @param userId 用户ID
	 * @return void
	 * @author zhengqingya
	 * @date 2020/4/15 11:33
	 */
	public static void logout(String tokenValue) {
		JwtUserBO ju = getLoginUser(tokenValue);
		StpUtil.logout(ju.getUserId());
		RedisUtil.delete(JWT_USER_KEY + tokenValue);
	}

	/**
	 * 获取登录用户信息
	 *
	 * @return 登录用户信息
	 * @author zhengqingya
	 * @date 2020/4/15 11:33
	 */
	public static JwtUserBO getLoginUser(String tokenValue) {
		String userObj = RedisUtil.get(JWT_USER_KEY + tokenValue);
		if (StrUtil.isBlank(userObj)) {
			throw NotLoginException.newInstance(StpUtil.getLoginType(), "用户未登录");
		}
		RedisUtil.setEx(JWT_USER_KEY + tokenValue, userObj, timeout, TimeUnit.SECONDS);
		return JSONUtil.toBean(userObj, JwtUserBO.class);
	}

	public static boolean isMobileDevice(String requestHeader) {
		String[] deviceArray = new String[] { "android", "iphone" };
		if (requestHeader == null)
			return false;
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
}
