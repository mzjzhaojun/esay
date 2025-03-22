package com.yt.app.common.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yt.app.common.util.IpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class Ytc {

	@Autowired
	Gson gson;

	@Pointcut("(execution(* com.yt.app.api.v1.controller.*.*(..)))")
	public void controllerPointCut() {
	}

	@Around("controllerPointCut()")
	public Object Around(ProceedingJoinPoint pjp) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object[] args = pjp.getArgs();
		StringBuffer sb = new StringBuffer();
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				HttpServletRequest p = (HttpServletRequest) arg;
				sb.append(p.getRequestURI()).append(" ");
				sb.append(p.getMethod()).append(" ");
				sb.append(IpUtil.getIpAddress(p)).append(" ");
				break;
			}
		}
		Object result = pjp.proceed(pjp.getArgs());
		long time = System.currentTimeMillis() - beginTime;
		log.info(">>>>>> " + sb.toString() + ">>>  Time = {} /ms", time);
		return result;
	}
}
