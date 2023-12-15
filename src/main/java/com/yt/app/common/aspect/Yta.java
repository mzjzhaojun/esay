package com.yt.app.common.aspect;

import com.google.gson.Gson;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.config.YtRedis;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.lang.Snowflake;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Aspect
@Component
public class Yta {

	@Autowired
	YtRedis c;

	@Autowired
	YtConfig d;

	@Autowired
	Snowflake e;

	@Autowired
	Gson g;

	@Pointcut("(execution(* com.yt.app.api.v1.mapper..*(..))) || (execution(* com.yt.app.common.base.YtIBaseMapper.*(..))) ")
	public void a() {
	}

	@Around("a()")
	public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Annotation annotation = null;
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		String key = getCacheKey(joinPoint.getSignature().getDeclaringType().getCanonicalName(), methodName,
				joinPoint.getArgs());
		Method me = ((MethodSignature) joinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(),
					proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				result = joinPoint.proceed(args);
				return result;
			}
		}
		if (annotation.annotationType().equals(YtRedisCacheEvictAnnotation.class)) {
			YtRedisCacheEvictAnnotation rcea = (YtRedisCacheEvictAnnotation) annotation;
			if (getMethodMatchesPost(methodName)) {
				Method mg = args[0].getClass().getDeclaredMethod("getId");
				Long id = (Long) mg.invoke(args[0]);
				if (id == null) {
					Method ms = args[0].getClass().getDeclaredMethod("setId", Long.class);
					ms.invoke(args[0], e.nextId());
				}
			}
			if (d.isCache()) {
				Class<?>[] classs = rcea.classs();
				for (Class<?> cl : classs) {
					RedisUtil.delete(cl.getName());
				}
			}
			result = joinPoint.proceed(args);
		} else if (annotation.annotationType().equals(YtRedisCacheAnnotation.class)) {
			if (d.isCache())
				result = RedisUtil.getExpire(key, TimeUnit.SECONDS);
			if (result != null) {
				return result;
			} else {
				result = joinPoint.proceed(args);
				if (result != null && d.isCache()) {
					RedisUtil.setEx(key, result, (long) c.getExpire(), TimeUnit.SECONDS);
				}
			}
		}
		return result;
	}

	private boolean getMethodMatchesPost(String method) {
		if (method.matches("add(.*)") || method.matches("post(.*)") || method.matches("sava(.*)")) {
			return true;
		}
		return false;
	}

	private String getCacheKey(String targetName, String methodName, Object[] arguments) {
		StringBuffer sbu = new StringBuffer();
		sbu.append(targetName).append("_" + d.getWorkerId() + "_").append(methodName);
		sbu.append("_").append(g.toJson(arguments));
		return sbu.toString();
	}
}