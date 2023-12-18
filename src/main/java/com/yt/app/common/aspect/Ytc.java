package com.yt.app.common.aspect;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yt.app.api.v1.entity.Logs;
import com.yt.app.api.v1.mapper.LogsMapper;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.RequestUtil;

@Aspect
@Component
public class Ytc {
	private static final Logger logger = LoggerFactory.getLogger(Ytc.class);

	private Pattern allowedMethods = Pattern.compile("^(HEAD|TRACE|OPTIONS|GET)$");

	@Autowired
	Gson gson;

	@Autowired
	LogsMapper logsmapper;

	@Pointcut("(execution(* com.yt.app.api.v1.controller.*Controller.*(..))) || (execution(* com.yt.app.common.base.YtIBaseEncipherController.*(..))) || (execution(* com.yt.app.common.base.YtIBaseController.*(..)))")
	public void controllerPointCut() {
	}

	@Around("controllerPointCut()")
	public Object Around(ProceedingJoinPoint pjp) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object[] args = pjp.getArgs();
		Logs log = new Logs(SysUserContext.getUsername(), DateTimeUtil.getNow(), 1);
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				HttpServletRequest p = (HttpServletRequest) arg;
				log.setRequesturl(p.getRequestURI());
				log.setRequestparams(RequestUtil.requestToParamMap(p).toString());
				log.setMethod(p.getMethod());
				log.setRequestip(p.getRemoteHost());
				break;
			}
		}
		logger.info("<<<<<<<<<<<<<<<<<<<<IN  {}   M={}   P={}",
				pjp.getSignature().getDeclaringType().getCanonicalName(), log.getRequesturl(), log.getRequestparams());
		Object result = pjp.proceed(args);
		long time = System.currentTimeMillis() - beginTime;
		log.setTime(Long.signum(time));
		if (log.getMethod() != null && !allowedMethods.matcher(log.getMethod()).matches()
				&& !log.getRequestparams().equals("{}")) {
			logsmapper.post(log);
		}
		logger.info(">>>>>>>>>>>>>>>>>>>> OUT  T={}", time);
		return result;
	}
}
