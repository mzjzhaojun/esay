package com.yt.app.common.aspect;

import com.google.gson.Gson;
import com.yt.app.common.annotation.YtRedisCacheAnnotation;
import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;
import com.yt.app.common.common.YtRoutingDataSource;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.config.YtRedis;
import com.yt.app.common.util.RedisHashUtil;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Aspect
@Component
public class Yta {

	@Autowired
	RedisHashUtil rs;

	@Autowired
	YtRedis c;

	@Autowired
	YtConfig d;

	@Autowired
	Snowflake e;

	@Autowired
	Gson g;

	@Autowired
	private YtRoutingDataSource j;

	@Pointcut("(execution(* com.yt.app.api.v1.mapper..*(..))) || (execution(* com.yt.app.common.base.YtIBaseMapper.*(..))) ")
	public void a() {
	}

	@Around("a()")
	public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Annotation annotation = null;
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		String classname = joinPoint.getSignature().getDeclaringType().getCanonicalName();
		String key = getCacheKey(classname, methodName, joinPoint.getArgs());
		Method me = ((MethodSignature) joinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				result = joinPoint.proceed(args);
				return result;
			}
		}
		if (annotation.annotationType().equals(YtRedisCacheEvictAnnotation.class)) {
			this.j.o();
			if (getMethodMatchesPost(methodName)) {
				Method mg = args[0].getClass().getDeclaredMethod("getId");
				Long id = (Long) mg.invoke(args[0]);
				if (id == null) {
					Long ids = e.nextId();
					Method ms = args[0].getClass().getDeclaredMethod("setId", Long.class);
					ms.invoke(args[0], ids);
				}
			}
			if (d.isCache()) {
				rs.deleteObjectEx(classname);
			}
			result = joinPoint.proceed(args);
		} else if (annotation.annotationType().equals(YtRedisCacheAnnotation.class)) {
			this.j.n();
			if (d.isCache()) {
				log.info("从缓存读取数据");
				result = rs.getObjectEx(classname, key);
			}
			if (result != null) {
				return result;
			} else {
				result = joinPoint.proceed(args);
				if (result != null && d.isCache()) {
					rs.setObjectEx(classname, key, result, c.getExpire(), TimeUnit.SECONDS);
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
		sbu.append("payboot:" + targetName).append(methodName).append(":").append(g.toJson(arguments));
		return sbu.toString();
	}
}