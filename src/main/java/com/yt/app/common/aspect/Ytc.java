package com.yt.app.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;

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
		Object result = pjp.proceed(pjp.getArgs());
		long time = System.currentTimeMillis() - beginTime;
		log.info(">>>>>>>>>>>>>>>>>>>> 处理时间  Time = {} /ms", time);
		return result;
	}
}
