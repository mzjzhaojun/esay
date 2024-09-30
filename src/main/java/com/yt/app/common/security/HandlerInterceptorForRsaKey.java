package com.yt.app.common.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.util.AesUtil;
import com.yt.app.common.util.RsaUtil;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 拦截器 -- rsakey
 * </p>
 *
 */
@Slf4j
public class HandlerInterceptorForRsaKey implements HandlerInterceptor {

	public HandlerInterceptorForRsaKey(YtConfig config) {
	}

	/**
	 * 
	 */
	private static final String SIGNA = "signa";

	private static final String SIGN = "sign";

	private static final String REAL_IP = "X-Forwarded-For";

	/**
	 * 在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制、权限校验等处理；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String ip = request.getHeader(REAL_IP);
		log.info(ip);
		if (ip != null && ip.indexOf(",") != -1) {
			String realip = ip.substring(0, ip.indexOf(","));
			AuthContext.setIp(realip);
		} else {
			AuthContext.setIp(ip);
		}
		String siang = request.getHeader(SIGNA);
		if (StringUtils.isNotBlank(siang)) {
			AuthContext.setKey(siang);
		}
		String key = AesUtil.getKey();
		String sign = RsaUtil.sign(key.getBytes(), RsaUtil.getPrivateKey());
		response.addHeader(SIGNA, key);
		response.addHeader(SIGN, sign);
		AuthContext.setAesKey(key);
		return true;
	}

	/**
	 * 在业务处理器处理请求执行完成后，生成视图之前执行。
	 * 后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		AuthContext.removeAll();
	}

}
