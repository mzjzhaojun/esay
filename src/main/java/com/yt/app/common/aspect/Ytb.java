package com.yt.app.common.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.common.YtRoutingDataSource;
import com.yt.app.common.enums.YtDataSourceEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class Ytb {

	@Autowired
	private YtRoutingDataSource j;

	@Pointcut("(execution(* com.yt.app.api.v1.service..*(..))) || (execution(* com.yt.app.common.base.YtIBaseService.*(..))) ")
	public void a() {
	}

	@Before("a()")
	public void a(JoinPoint paramJoinPoint) throws Throwable {
		Method me = ((MethodSignature) paramJoinPoint.getSignature()).getMethod();
		YtDataSourceAnnotation rcea = me.getAnnotation(YtDataSourceAnnotation.class);
		if (rcea != null) {
			if (rcea.datasource().equals(YtDataSourceEnum.SLAVE)) {
				log.info("使用从数据库");
				this.j.n();
			} else {
				log.info("使用主数据库");
				this.j.o();
			}
		} else {
			this.j.o();
		}
	}

	@After("a()")
	public void b(JoinPoint paramJoinPoint) {
		this.j.p();
	}
}