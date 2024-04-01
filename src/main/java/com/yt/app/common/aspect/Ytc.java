package com.yt.app.common.aspect;

import java.util.regex.Pattern;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yt.app.api.v1.dbo.AuthLoginDTO;
import com.yt.app.api.v1.entity.Logs;
import com.yt.app.api.v1.mapper.LogsMapper;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.util.DateTimeUtil;

@Aspect
@Component
public class Ytc {
	private static final Logger logger = LoggerFactory.getLogger(Ytc.class);

	private Pattern allowedMethods = Pattern.compile("^(HEAD|TRACE|OPTIONS|GET)$");

	@Autowired
	Gson gson;

	@Autowired
	LogsMapper logsmapper;

	@Pointcut("(execution(* com.yt.app.api.v1.controller.AuthAppController.*(..))) || (execution(* com.yt.app.api.v1.controller.AuthController.*(..)))")
	public void controllerPointCut() {
	}

	@Around("controllerPointCut()")
	public Object Around(ProceedingJoinPoint pjp) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object[] args = pjp.getArgs();
		Logs log = new Logs(SysUserContext.getUsername(), DateTimeUtil.getNow(), 1);
		for (Object arg : args) {
			if (arg instanceof YtRequestDecryptEntity) {
				@SuppressWarnings("unchecked")
				YtRequestDecryptEntity<AuthLoginDTO> p = (YtRequestDecryptEntity<AuthLoginDTO>) arg;
				log.setRequesturl(p.getUrl().getPath());
				log.setRequestparams(p.getBody().toString());
				log.setMethod(p.getMethod().name());
				log.setRequestip(p.getUrl().getHost());
				break;
			}
		}
		logger.info("<<<<<<<<<<<<<<<<<<<<IN  {}   M={}   P={}",
				pjp.getSignature().getDeclaringType().getCanonicalName(), log.getRequesturl(), log.getRequestparams());
		Object result = pjp.proceed(args);
		long time = System.currentTimeMillis() - beginTime;
		log.setTime(Long.signum(time));
		if (log.getMethod() != null && !allowedMethods.matcher(log.getMethod()).matches()) {
			logsmapper.post(log);
		}
		logger.info(">>>>>>>>>>>>>>>>>>>> OUT  T={}", time);
		return result;
	}
}
