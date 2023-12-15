package com.yt.app.common.aspect;

import java.lang.annotation.Annotation;
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
import com.yt.app.common.common.Yte;
import com.yt.app.common.enums.YtDataSourceEnum;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Aspect
@Component
public class Ytb {

	@Autowired
	private Yte j;

	@Pointcut("(execution(* com.yt.app.api.v1.service..*(..))) || (execution(* com.yt.app.common.base.YtIBaseService.*(..))) ")
	public void a() {
	}

	@Before("a()")
	public void a(JoinPoint paramJoinPoint) throws Throwable {
		Annotation annotation = null;
		Method me = ((MethodSignature) paramJoinPoint.getSignature()).getMethod();
		annotation = me.getAnnotations().length > 0 ? me.getAnnotations()[0] : null;
		if (annotation == null) {
			Method proxyMethod = ((MethodSignature) paramJoinPoint.getSignature()).getMethod();
			Method mes = proxyMethod.getDeclaringClass().getMethod(proxyMethod.getName(),
					proxyMethod.getParameterTypes());
			annotation = mes.getAnnotations().length > 0 ? mes.getAnnotations()[0] : null;
			if (annotation == null) {
				this.j.o();
			}
		} else if (annotation.annotationType().equals(YtDataSourceAnnotation.class)) {
			YtDataSourceAnnotation rcea = (YtDataSourceAnnotation) annotation;
			if (rcea.datasource().equals(YtDataSourceEnum.SLAVE)) {
				this.j.n();
			} else {
				this.j.o();
			}
		}
	}

	@After("a()")
	public void b(JoinPoint paramJoinPoint) {
		this.j.p();
	}
}